package okhttp;

import com.google.gson.Gson;
import dto.AllContactsDto;
import dto.ContactDto;
import dto.ErrorDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContactsOkhttp {
    Gson gson= new Gson();
    OkHttpClient client = new OkHttpClient();
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoidmFsKzFAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE2ODE5ODIxNzgsImlhdCI6MTY4MTM4MjE3OH0.uEv7-blcXfZmOVkTpPePWyab1bdYF3PW8vfvtrwOXG0";

    @Test
    public void getAllContactSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .get().build();


        Response response = client.newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());

        AllContactsDto allContacts = gson.fromJson(response.body().string(), AllContactsDto.class);
        List<ContactDto> contacts = allContacts.getContacts();
        for(ContactDto contact:contacts){
            System.out.println(contact.getId());
            System.out.println("============");
        /*ec0278f4-33c7-4001-a270-605d11258e2c
        }
============
a97dbf15-923a-4e9a-bd6e-9036907d2d94
============

d048003f-5e34-4430-a350-a85d9089f165*/
    }
    }
    @Test
    public void getAllContactNegative() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization","sdfsdf")
                .get().build();

        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(),401);

        ErrorDto error = gson.fromJson(response.body().string(),ErrorDto.class);
        System.out.println(error.getMessage().toString());
        Assert.assertEquals(error.getMessage().toString(),"JWT strings must contain exactly 2 period characters. Found: 0");
        System.out.println(error.getError());
        Assert.assertEquals(error.getMessage().toString(),"JWT strings must contain exactly 2 period characters. Found: 0");
        Assert.assertEquals(error.getError(),"Unauthorized");

        /*ec0278f4-33c7-4001-a270-605d11258e2c*/
    }
}
