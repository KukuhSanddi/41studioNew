package com.kukuh.studio;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
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
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kukuh.studio.Visitor;
import com.kukuh.studio.Database;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainVisitor extends AppCompatActivity {
    //-----------------------------------------------------------//
    //Var XML
    private EditText inputName, inputEmail, inputNo, inputKep;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutNo, inputLayoutKep;
    private ImageButton btnFoto;
    private TextView jdlFoto;
    private String prompt;
    private String emailEmp;



    //Var untuk Database visitor
    Visitor vis;
    Database database = new Database();
    FirebaseDatabase fbase = FirebaseDatabase.getInstance();
    private AutoCompleteTextView spinner;


    //Var untuk foto dan upload foto
    String mCurrentPhotoPath;
    private StorageReference mStorRef;
    private Uri filePath;
    public int REQUEST_TAKE_PHOTO = 1;
    public String urlFoto;
    String[] listArr;


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

        getNamaEmployee();

        ImageView img = findViewById(R.id.long_logo);
        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputNo = findViewById(R.id.input_phone);
        inputKep = findViewById(R.id.input_kep);
        spinner = findViewById(R.id.dropdown);

        spinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    spinner.showDropDown();

                } else {

                }
            }
        });



        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });


        findViewById(R.id.visitor).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                return true;
            }
        });

        inputLayoutName = findViewById(R.id.input_layout_name);
        inputLayoutEmail = findViewById(R.id.input_layout_email);
        inputLayoutNo = findViewById(R.id.input_layout_phone);
        inputLayoutKep = findViewById(R.id.input_layout_keperluan);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputNo.addTextChangedListener(new MyTextWatcher(inputNo));
        inputKep.addTextChangedListener(new MyTextWatcher(inputKep));


        final String namaVis = inputName.getText().toString();

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
                        && (validatePhone()) && (validateKep()) && (validateSpinner())){
                    vis = new Visitor(namaVis,emailVis,noVis,jamCheckin,"",kepVis,urlFoto);
                    database.checkinVis(vis);
                    Log.e("adloy",emailEmp);

//                    sendEmail();
                    Intent intent = new Intent(MainVisitor.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(getApplicationContext(), Home.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();

    }

    /**
     * new session
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
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
       if (spinner.getText().toString().trim().isEmpty()){
           spinner.setError(getString(R.string.err_msg_form));
           spinner.setHint(getString(R.string.err_msg_form));
           spinner.setTextColor(Color.RED);
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
            inputName.setError(getString(R.string.err_msg_form));
            inputLayoutName.setError(" ");
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
            inputEmail.setError(getString(R.string.err_msg_email));
            inputLayoutEmail.setError(" ");
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
            inputNo.setError((getString(R.string.err_msg_phone)));
            inputLayoutNo.setError(" ");
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
            inputKep.setError(getString(R.string.err_msg_form));
            inputLayoutKep.setError(" ");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            jdlFoto = findViewById(R.id.jdlFoto);
            try{
                jdlFoto.setText(createImageFile().getName());
            }
            catch (IOException e){

            }
            uploadImage();
            btnFoto.setImageURI(filePath);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Compressing image to bitmap
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] data = baos.toByteArray();

            //Uploading bitmap to firebase
            uploadTask = ref.putBytes(data);
            final StorageReference finalRef = ref;
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    finalRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            urlFoto = downloadUrl.toString();
                        }
                    });
                    Toast.makeText(MainVisitor.this,"Uploaded",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //Create image.jpg file
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
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

    //Read data employee from database
    public void getNamaEmployee(){
        final ArrayList<Employee> listNama = new ArrayList<>();
        final DatabaseReference dRef = fbase.getReference("employees").child("dataKaryawan");

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listNama.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Employee emp = data.getValue(Employee.class);
                    listNama.add(emp);
                }

                listArr = new String[listNama.size()];
                for (int i=0; i<listNama.size();i++){
                    listArr[i]=listNama.get(i).getNama();
                }

                String[] newArr = new String[listArr.length];
                for (int i = 0; i<newArr.length; i++){
                    newArr[i] = listArr[i];
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainVisitor.this, R.layout.spinner_item, newArr);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        emailEmp = listNama.get(i).getEmail();
                        Toast.makeText(MainVisitor.this, emailEmp.toString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        setTitle(R.string.selected_one);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
