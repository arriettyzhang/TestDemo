package com.example.arrietty.demoapp.inflectdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.arrietty.demoapp.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by asus on 2017/12/7.
 */

public class InflectActivity extends AppCompatActivity {
    private static Person person;
    private static Class<Person> cls;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflect);
        person = new Person("Zhao");
        cls = (Class<Person>) person.getClass();

        creatClassByReflection();
        printAllMethods();
        printAllFileds();
        invokePrivateMothod();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * 利用反射创建对象
     */
    private static void creatClassByReflection() {
        try {
            Person accpTeacher = (Person) Class.forName("edu.qust.demo.Person")
                    .newInstance();
            System.out.println(accpTeacher.toString());
            System.out.println();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取并调用私有方法
     */
    private static void invokePrivateMothod() {
        try {
            // 获取方法名为showName，参数为String类型的方法
            Method method = cls.getDeclaredMethod("showName", String.class);
            // 若调用私有方法，必须抑制java对权限的检查
            method.setAccessible(true);
            // 使用invoke调用方法，并且获取方法的返回值，需要传入一个方法所在类的对象，new Object[]
            // {"Kai"}是需要传入的参数，与上面的String.class相对应
            String string = (String) method.invoke(person,
                    new Object[] { "Kai" });
            System.out.println(string);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取并打印所有的字段名
     */
    private static void printAllFileds() {
        Field[] field = cls.getDeclaredFields();
        System.out.println("getFields():获取所有权限修饰符修饰的字段");
        for (Field f : field) {
            System.out.println("Field Name = " + f.getName());
        }
        System.out.println();
    }

    /**
     * 获取并打印所有的方法名
     */
    private static void printAllMethods() {
        Method[] method = cls.getDeclaredMethods();
        System.out.println("getDeclaredMethods():获取所有的权限修饰符修饰的Method");
        for (Method m : method) {
            System.out.println("Method Name = " + m.getName());
        }
        System.out.println();
    }
}
