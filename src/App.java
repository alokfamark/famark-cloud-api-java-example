import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.DataOutputStream;
import java.io.IOException;

public class App {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Domain Name: ");
        String domainName = input.next();
        System.out.println("Enter User Name: ");
        String userName = input.next();
        System.out.println("Enter Password: ");
        String password = input.next();
        HashMap<String, Object> credData = new HashMap<String, Object>();
        credData.put("DomainName", domainName);
        credData.put("UserName", userName);
        credData.put("Password", password);
        JSONObject credDataJson = new JSONObject(credData);
        String request = credDataJson.toJSONString();
        Reader reader = postData("/Credential/Connect", request, null);
        Object obj = JSONValue.parse(reader);
        String sessionID = (String) obj;
        System.out.println(sessionID);
        if (sessionID == null || sessionID.length() == 0) {
            input.close();
            return;
        }

        // Creating records by taking input from user
        System.out.println("Do you want to create a new record (y/n): ");
        char userResponse = input.next().charAt(0);
        while (userResponse == 'Y' || userResponse == 'y') {
            // Clearing new line from buffer
            input.nextLine();
            System.out.println("Enter FirstName: ");
            String firstName = input.nextLine();
            System.out.println("Enter LastName: ");
            String lastName = input.nextLine();
            System.out.println("Enter Phone: ");
            String phone = input.nextLine();
            System.out.println("Enter Email: ");
            String email = input.nextLine();
            HashMap<String, Object> contactData = new HashMap<String, Object>();
            contactData.put("FirstName", firstName);
            contactData.put("LastName", lastName);
            contactData.put("Phone", phone);
            contactData.put("Email", email);
            JSONObject contactDataJson = new JSONObject(contactData);
            request = contactDataJson.toJSONString();
            reader = postData("/Business_Contact/CreateRecord", request, sessionID);
            obj = JSONValue.parse(reader);
            String recordID = (String) obj;
            if (recordID != null && recordID.length() > 0) {
                System.out.println("Created RecordId: \n" + recordID);
                System.out.println("Do you want to create more System record (y/n)? ");
            } else {
                System.out.println("Do you want to try again (y/n)? ");
            }
            userResponse = input.next().charAt(0);
        }
        // input.close();

        // Calling RetrieveMultipleRecords action on the System_Profile entity
        HashMap<String, Object> retrieveData = new HashMap<String, Object>();
        retrieveData.put("Columns", "FirstName,LastName,Phone,Email,Business_ContactId");
        retrieveData.put("OrderBy", "FirstName");
        JSONObject retrieveDataJson = new JSONObject(retrieveData);
        request = retrieveDataJson.toJSONString();
        reader = postData("/Business_Contact/RetrieveMultipleRecords", request, sessionID);
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(reader);

        // Checking whether the JSON array has some value or not
        if (array != null) {

            // Iterating JSON array
            System.out.println("Records are: ");
            for (int i = 0; i < array.size(); i++) {
                System.out.println(i + 1 + ": " + array.get(i));
            }
        }
        
        // Deleting records by taking input from user
        System.out.println("Do you want delete a record (y/n): ");
        char userDeleteResponse = input.next().charAt(0);
        while (userDeleteResponse == 'Y' || userDeleteResponse == 'y') {
            input.nextLine();
            System.out.println("Enter ContactId: ");
            String contactId = input.nextLine();
            HashMap<String, Object> businessData = new HashMap<String, Object>();
            businessData.put("Business_ContactId", contactId);
            JSONObject businessDataJson = new JSONObject(businessData);
            request = businessDataJson.toJSONString();
            postData("/Business_Contact/DeleteRecord", request, sessionID);
            System.out.println("Record Deleted");
            System.out.println("Do you want delete a record (y/n): ");
            userDeleteResponse = input.next().charAt(0);
        }

        // Updating records by taking user from user
        System.out.println("Do you want update a record (y/n): ");
        char userUpdateResponse = input.next().charAt(0);
          while (userUpdateResponse == 'Y' || userUpdateResponse == 'y') {
            // Clearing new line from buffer
            input.nextLine();
            System.out.println("Enter ContactId: ");
            String contactId = input.nextLine();
            System.out.println("Enter FirstName: ");
            String firstName = input.nextLine();
            System.out.println("Enter LastName: ");
            String lastName = input.nextLine();
            System.out.println("Enter Phone: ");
            String phone = input.nextLine();
            System.out.println("Enter Email: ");
            String email = input.nextLine();
            HashMap<String, Object> updateData = new HashMap<String, Object>();
            updateData.put("Business_ContactId", contactId);
            updateData.put("FirstName", firstName);
            updateData.put("LastName", lastName);
            updateData.put("Phone", phone);
            updateData.put("Email", email);
            JSONObject updateDataJson = new JSONObject(updateData);
            request = updateDataJson.toJSONString();
            postData("/Business_Contact/UpdateRecord", request, sessionID);
            System.out.println("Record Updated");
            System.out.println("Do you want update a record (y/n): ");
            userUpdateResponse = input.next().charAt(0);
        }
        input.close();
    }

    private static Reader postData(String urlSuffix, String body, String sessionID) throws IOException {
        URL url = new URL("https://www.famark.com/host/api.svc/api" + urlSuffix);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        byte[] out = body.getBytes(StandardCharsets.UTF_8);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.setRequestProperty("Content-Length", Integer.toString(out.length));
        if (sessionID != null && sessionID.length() > 0) {
            http.setRequestProperty("SessionId", sessionID);
        }
        try (DataOutputStream os = new DataOutputStream(http.getOutputStream())) {
            os.write(out);
        }
        String errorMessage = http.getHeaderField("ErrorMessage");
        if (errorMessage != null && errorMessage.length() > 0) {
            System.err.println(errorMessage);
            return null;
        }
        return new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
    }
}
