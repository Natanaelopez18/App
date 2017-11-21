package com.uca.apps.isi.nct.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tumblr.remember.Remember;
import com.uca.apps.isi.nct.R;
import com.uca.apps.isi.nct.api.Api;
import com.uca.apps.isi.nct.models.Complaint;
import com.uca.apps.isi.nct.models.Location;
import com.uca.apps.isi.nct.models.Picture;

import java.io.File;

public class ComplaintAddActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private Button create;
    private ProgressDialog mprogressDialog;
    private StorageReference storageReference;
    private final String Carpeta_Raiz="misImagenes/";
    private final String Ruta_Imagen=Carpeta_Raiz+"misFotos";
    final  int Cod_Selecciona=10;
    final  int Cod_Foto=20;
    ImageView imagen;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_add);
        initViews();
    }

    /**
     * To init views on variables
     */
    private void initViews() {
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        create = (Button) findViewById(R.id.create);
        storageReference = FirebaseStorage.getInstance().getReference();
        mprogressDialog = new ProgressDialog(this);
        imagen = (ImageView) findViewById(R.id.imagenes);
    }

    private void create() {

        if (title.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Requiero un titulo", Toast.LENGTH_LONG).show();
        } else if(description.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Requiero una descripcion", Toast.LENGTH_LONG).show();
        } else {
            // this is for create a progress dialog
            mprogressDialog.setTitle("Cargando");
            mprogressDialog.setMessage("Creando denuncia");
            mprogressDialog.setCancelable(false);
            mprogressDialog.show();
            // this instance new complaint with data xD
            Complaint complaint = new Complaint();
            complaint.setTitle(title.getText().toString());
            complaint.setDescription(description.getText().toString());
            complaint.setCategoryId(1);
            complaint.setCreatedAt("2017-11-08T05:49:16.827Z");
            complaint.setEnabled(true);

            // instance location
            Location location = new Location();
            location.setLat(15.8);
            location.setLng(15.4);

            complaint.setLocation(location);



            // this make http request to create an complaint
            Call<Complaint> call = Api.instance().createComplaint(complaint,Remember.getString("access_token", ""));
            call.enqueue(new Callback<Complaint>() {
                @Override
                public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                    if (response.body() != null) {
                        Complaint complaintResult  = response.body();
                        assert complaintResult != null;
                        mprogressDialog.dismiss();
                        Log.i("title", complaintResult.getTitle());
                        Log.i("description", complaintResult.getDescription());
                        Toast.makeText(getApplicationContext(),"Se creo la denuncia con exito",Toast.LENGTH_LONG).show();
                        LimpiarCampos();
                    }else{
                        Toast.makeText(getApplicationContext(),"No se creo la denuncia",Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Complaint> call, Throwable t) {
                    Log.e("Error", t.getMessage());

                }
            });


        }
    }
    public void LimpiarCampos(){
        Uri uri = null;
        title.setText("");
        description.setText("");
        imagen.setImageURI(uri);

    }

    public void crear(View view) {
        create();
    }

    public void Mensaje(){

        final CharSequence[] opciones={"Tomar Foto","Cargar Foto","Cancelar"};
        final AlertDialog.Builder alertOpciones= new AlertDialog.Builder(ComplaintAddActivity.this);
        alertOpciones.setTitle("Selecione una Opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar Foto")){
                    tomarFoto();
                }else{
                    if(opciones[i].equals("Cargar Foto")){
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent,"Seleccione una app"),Cod_Selecciona);
                    }else{
                        dialogInterface.dismiss();
                    }
                }

            }
        });
        alertOpciones.show();
    }

    public void tomarFoto(){
        File fileImagen =  new File(Environment.getExternalStorageDirectory(),Ruta_Imagen);
        boolean isCreada = fileImagen.exists();
        String nombreImagen="";
        if(isCreada==false){
            isCreada = fileImagen.mkdirs();
        }
        if(isCreada==true){
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }

        path= Environment.getExternalStorageDirectory()+File.separator+Ruta_Imagen+File.separator+nombreImagen;
        File imagen =  new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        startActivityForResult(intent,Cod_Foto);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){

            switch (requestCode){
                case Cod_Selecciona:
                    Uri uri = data.getData();
                    StorageReference filePath = storageReference.child(Ruta_Imagen).child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri descargarFoto = taskSnapshot.getDownloadUrl();

                            Glide.with(ComplaintAddActivity.this)
                                    .load(descargarFoto)
                                    .fitCenter()
                                    .crossFade()
                                    .into(imagen);
                            Toast.makeText(getApplicationContext(),"Listo",Toast.LENGTH_LONG).show();
                            createPictures(descargarFoto);

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Fallo el subir la imagen",Toast.LENGTH_LONG).show();
                        }
                    });
                    
                    break;
                case Cod_Foto:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String s, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);
                    break;
            }


        }
    }

    public void caragar(View view) {
        Mensaje();
    }


    public void createPictures(Uri uri){

        //instance picture

        Picture picture = new Picture();
        picture.setTitle(title.getText().toString());
        picture.setUrl(uri.toString());
        picture.setEnable(true);
        picture.setComplaintId(77);

        Call<Picture> call = Api.instance().createPicture(picture,Remember.getString("access_token", ""));
        call.enqueue(new Callback<Picture>() {
            @Override
            public void onResponse(Call<Picture> call, Response<Picture> response) {
                if (response.body() != null) {
                    Picture pictureResult  = response.body();
                    assert pictureResult != null;
                    Toast.makeText(getApplicationContext(),"Se creo la imagen con exito",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No se creo la imagen",Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Picture> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }

        });
    }
}
