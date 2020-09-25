package com.vbqkma.libarybackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;


@Controller
@RequestMapping("/rest/vubaquang")
public class TestController {
    @GetMapping(path = "/get-cookie")
    public @ResponseBody
    ResponseEntity<String> login(@RequestParam String cookie) {
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream("cookie.txt",true);
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
//            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
//            Date date = new Date();
//            bufferedWriter.write(date.toString() + ": " +cookie);
//            bufferedWriter.newLine();
//            bufferedWriter.close();
//            outputStreamWriter.close();
//            fileOutputStream.close();
//            System.out.println("Get cookie success !");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return new ResponseEntity<>("<h1>Vũ Bá Quang - AT130444 - Get cookie success !</h1>", null, HttpStatus.OK);
    }
}