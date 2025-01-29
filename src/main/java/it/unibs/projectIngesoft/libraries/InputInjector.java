package it.unibs.projectIngesoft.libraries;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class InputInjector {


    public static void inject(String data){

        ByteArrayInputStream inputBytes = new ByteArrayInputStream(data.getBytes());

        System.setIn(inputBytes);
    }
}
