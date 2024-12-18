package com.example.appchat.providers;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.appchat.model.Post;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostProvider {
    public LiveData<String> addPost(Post post) {
        MutableLiveData<String> result = new MutableLiveData<>();
        ParseObject postObject = new ParseObject("Post");
        postObject.put("titulo", post.getTitulo());
        postObject.put("descripcion", post.getDescripcion());
        postObject.put("duracion", post.getDuracion());
        postObject.put("categoria", post.getCategoria());
        postObject.put("presupuesto", post.getPresupuesto());
        postObject.put("user", ParseUser.getCurrentUser());  // Relación con el usuario
        postObject.saveInBackground(e -> {
            if (e == null) {
                ParseRelation<ParseObject> relation = postObject.getRelation("images");
                for (String url : post.getImagenes()) {
                    ParseObject imageObject = new ParseObject("Image");
                    imageObject.put("url", url);
                    imageObject.saveInBackground(imgSaveError -> {
                        if (imgSaveError == null) {
                            relation.add(imageObject);
                            postObject.saveInBackground(saveError -> {
                                if (saveError == null) {
                                    result.setValue("Post publicado");
                                } else {
                                    result.setValue("Error al guardar la relación con las imágenes: " + saveError.getMessage());
                                }
                            });
                        } else {
                            result.setValue("Error al guardar la imagen: " + imgSaveError.getMessage());
                        }
                    });
                }
            } else {
                result.setValue("Error al guardar el post: " + e.getMessage());
            }
        });
        return result;
    }

    public LiveData<List<Post>> getPostsByCurrentUser() {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            result.setValue(new ArrayList<>());
            return result;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("user", currentUser); // Filtrar por el usuario actual
        query.include("user"); // Incluir la relación de usuario si es necesario
        query.findInBackground((posts, e) -> {
            if (e == null) {
                List<Post> postList = new ArrayList<>();
                for (ParseObject postObject : posts) {
                    Post post = new Post(
                            postObject.getString("titulo"),
                            postObject.getString("descripcion"),
                            postObject.getInt("duracion"),
                            postObject.getString("categoria"),
                            postObject.getDouble("presupuesto")
                    );
                    Log.d("PostProvider ", "post: "+post.getTitulo()+post.getCategoria());
                    // Agregar imágenes (si las necesitas en este paso)
                    ParseRelation<ParseObject> relation = postObject.getRelation("images");
                    try {
                        List<ParseObject> images = relation.getQuery().find();
                        List<String> imageUrls = new ArrayList<>();
                        for (ParseObject imageObject : images) {
                            imageUrls.add(imageObject.getString("url"));
                        }
                        post.setImagenes(imageUrls);
                        Log.d("PostProvider ", "img: "+post.getImagenes());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }

                    postList.add(post);
                }
                result.setValue(postList);
            } else {
                result.setValue(new ArrayList<>());
                Log.e("ParseError", "Error al recuperar los posts: ", e);
            }
        });

        return result;
    }

}





