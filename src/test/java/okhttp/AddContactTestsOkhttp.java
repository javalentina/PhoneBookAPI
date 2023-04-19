package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class AddContactTestsOkhttp {
    private final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoidmFsKzFAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE2ODE5ODIxNzgsImlhdCI6MTY4MTM4MjE3OH0.uEv7-blcXfZmOVkTpPePWyab1bdYF3PW8vfvtrwOXG0";

    @Test
    public void addContactSuccessTest() throws IOException {
        int i = new Random().nextInt(1000) + 1000;
        ContactDto contactDto = ContactDto.builder()
                .name("Jet")
                .lastName("Jetson").email("jet"+i+"@gmsil.com")
                .phone("123123"+i)
                .address("Berlin")
                .description("sdfs").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());

        ContactResponseDto contactResponse = gson.fromJson(response.body().string(), ContactResponseDto.class);
        System.out.println(contactResponse.getMessage());
        Assert.assertTrue(contactResponse.getMessage().contains("Contact was added!"));
    }

    @Test
    public void addContactWithoutNameTest() throws IOException {

        ContactDto contactDto = ContactDto.builder()
                .lastName("Jetson").email("jet@gmsil.com")
                .phone("123123123123")
                .address("Berlin")
                .description("real hero").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),400);

        ErrorDto error = gson.fromJson(response.body().string(),ErrorDto.class);
        System.out.println(error.getMessage().toString());
        Assert.assertEquals(error.getMessage().toString(),"{name=must not be blank}");


    }
    @Test
    public void addContactWithWrongEmailTest() throws IOException {

        ContactDto contactDto = ContactDto.builder().name("Jet")
                .lastName("Jetson").email("jetgmsil.com")
                .phone("123123123123")
                .address("Berlin")
                .description("real hero").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),400);

        ErrorDto error = gson.fromJson(response.body().string(),ErrorDto.class);
        Assert.assertEquals(error.getMessage().toString(),"{email=must be a well-formed email address}");


    }

    @Test
    public void addSameContactTest() throws IOException {

        ContactDto contactDto = ContactDto.builder().name("Jet")
                .lastName("Jetson").email("jet@gmsil.com")
                .phone("123123123123")
                .address("Berlin")
                .description("real hero").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),409);

        ErrorDto error = gson.fromJson(response.body().string(),ErrorDto.class);
        System.out.println(error.getMessage().toString());
        Assert.assertEquals(error.getMessage().toString(),"{name=must not be blank}");


    }

}
