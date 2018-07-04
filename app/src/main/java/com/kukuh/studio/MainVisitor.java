package com.kukuh.studio;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kukuh.studio.Visitor;
import com.kukuh.studio.Database;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainVisitor extends AppCompatActivity {
//    Spinner spinner = (Spinner) findViewById(R.id.dropdown);
//    ArrayAdapter<CharSequence> adapter = ArrayAdapter
//            .createFromResource(this,R.array.item_dropdown,
//                    android.R.layout.simple_spinner_item);

    // Specify the layout to use when the list of choices appears

//    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    // Apply the adapter to the spinner
//    spinner.setAdapter(adapter);


//    -----------------------------------------------------------

    private EditText inputName, inputEmail, inputNo, inputKep;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutNo, inputLayoutKep;
    private ImageButton btnFoto;
    private TextView jdlFoto;


    //Var untuk Database visitor
    Visitor vis;
    Database database = new Database();
    private Spinner spinner;


    //Var untuk foto dan upload foto
    String mCurrentPhotoPath;
    private StorageReference mStorRef;
    private Uri filePath;
    public int REQUEST_TAKE_PHOTO = 1;
    public String namaFoto;
    public String urlFoto = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_visitor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat jamFormat = new SimpleDateFormat("HH:mm:ss");
        final String jamCheckin = jamFormat.format(calendar.getTime());

        mStorRef = FirebaseStorage.getInstance().getReference();


        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputNo = findViewById(R.id.input_phone);
        inputKep = findViewById(R.id.input_kep);
        spinner = findViewById(R.id.dropdown);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainVisitor.this, R.array.employee,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        inputLayoutName = findViewById(R.id.input_layout_name);
        inputLayoutEmail = findViewById(R.id.input_layout_email);
        inputLayoutNo = findViewById(R.id.input_layout_phone);
        inputLayoutKep = findViewById(R.id.input_layout_keperluan);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputNo.addTextChangedListener(new MyTextWatcher(inputNo));
        inputKep.addTextChangedListener(new MyTextWatcher(inputKep));

        btnFoto = findViewById(R.id.btnFoto);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            btnFoto.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String namaVis = inputName.getText().toString();
                final String emailVis = inputEmail.getText().toString();
                final String noVis = inputNo.getText().toString();
                final String kepVis = inputKep.getText().toString();

                submitForm();
                if ((validateName())&& (validateEmail())
                        && (validatePhone()) && (validateKep())){
                    vis = new Visitor(namaVis,emailVis,noVis,jamCheckin,"",kepVis,urlFoto);
                    database.checkinVis(vis);
//                    sendEmail();
                    Intent intent = new Intent(MainVisitor.this, Home.class);
                    startActivity(intent);
                }

            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

    }


    /**
     * new session
     */
    @Override
    protected void onPause() {
        super.onPause();
        jdlFoto = findViewById(R.id.jdlFoto);
        try{
            jdlFoto.setText(createImageFile().getName());
        }
        catch (IOException e){

        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        uploadImage();
        btnFoto.setImageURI(filePath);
    }

    /**
     * Validating Form
     */

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }

        if (!validateSpinner()){
            return;
        }

        if (!validateKep()){
            return;
        }

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Spinner Validation
     */
    private boolean validateSpinner(){
       if (spinner.getSelectedItem().toString().trim().equals("Yang akan anda temui")){
           TextView erorText = (TextView) spinner.getSelectedView();
           erorText.setError("No one selected");
           erorText.setTextColor(Color.RED);
           erorText.setText("Pilih orang yang akan anda temui");
           requestFocus(spinner);
           return false;
       }

        return true;
    }


    /**
     *
     * Name Validation
     */
    private boolean validateName(){
        if (inputName.getText().toString().trim().isEmpty()){
            inputLayoutName.setError(getString(R.string.err_msg_form));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    /**
     *
     * Email Validation
     */
    private boolean validateEmail(){
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty()||!isValidEmail(email)){
            inputLayoutEmail.setError((getString(R.string.err_msg_email)));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    /**
     *
     * Phone Validation
     */
    private boolean validatePhone(){
        String numb = inputNo.getText().toString().trim();

        if (numb.isEmpty()||!isValidPhone(numb)){
            inputLayoutNo.setError((getString(R.string.err_msg_phone)));
            requestFocus(inputNo);
            return false;
        } else {
            inputLayoutNo.setErrorEnabled(false);
        }
        return true;
    }

    /**
     *
     * Validatoon multiple row
     */

    private boolean validateKep(){
        if (inputKep.getText().toString().trim().isEmpty()){
            inputLayoutKep.setError(getString(R.string.err_msg_form));
            requestFocus(inputKep);
            return false;
        } else {
            inputLayoutKep.setErrorEnabled(false);
        }
        return true;
    }

    /**
     *
     * Email format
     */

    private static boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     *
     * Phone Format
     */

    private static boolean isValidPhone(String phone){
        return !TextUtils.isEmpty(phone) && android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private void requestFocus(View view){
        if (view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view){
            this.view= view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
        }

        public void afterTextChanged(Editable editable){
            switch (view.getId()){
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_layout_phone:
                    validatePhone();
                    break;
                case R.id.input_layout_keperluan:
                    validateKep();
                    break;
            }
        }
    }

    //Sending Email
    private void sendEmail() {
        final String namaVis = inputName.getText().toString();
        final String kepVis = inputKep.getText().toString();

        //Getting content for email
        String email = "adli.rahman23@gmail.com";
        String subject = "41Studio Visitor";
        String message = namaVis+" sedang menunggu dibawah dan ingin bertemu dengan anda, dengan keperluan "+kepVis+".";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }


    /*
    /Camera and Upload
     */
    //Starting camera
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.kukuh.studio.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                filePath = Uri.fromFile(photoFile);
            }
        }

    }

    //Upload image to Firebase Storage
    public void uploadImage()  {
        UploadTask uploadTask;
        if(filePath != null)
        {
            StorageReference ref = null;
            try {
                ref = mStorRef.child("images/visitorID/"+createImageFile().getName());
                namaFoto = createImageFile().getName();
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadTask = ref.putFile(filePath);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainVisitor.this,"Uploaded",Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    //Create image.jpg file
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMMMyyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnFoto.setEnabled(true);
            }
        }
    }

}
