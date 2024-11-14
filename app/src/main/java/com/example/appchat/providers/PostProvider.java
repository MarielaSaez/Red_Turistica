package com.example.appchat.providers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appchat.model.Post;



import com.parse.ParseObject;

import com.parse.ParseRelation;
import com.parse.ParseUser;



public class PostProvider {

    // Método para agregar un post a la tabla de posts en Parse
    public LiveData<String> addPost(Post post) {
        MutableLiveData<String> result = new MutableLiveData<>();

        // Crear un nuevo ParseObject para el post
        ParseObject postObject = new ParseObject("Post");
        postObject.put("titulo", post.getTitulo());
        postObject.put("descripcion", post.getDescripcion());
        postObject.put("duracion", post.getDuracion());
        postObject.put("categoria", post.getCategoria());
        postObject.put("presupuesto", post.getPresupuesto());

        // Relacionar el post con el usuario actual (ParseUser.getCurrentUser())
        postObject.put("user", ParseUser.getCurrentUser());  // Relación con el usuario

        // Guardar el post en Parse
        postObject.saveInBackground(e -> {
            if (e == null) {
                // Si el post se guarda correctamente, agregar las imágenes
                ParseRelation<ParseObject> relation = postObject.getRelation("images");

                // Subir cada imagen
                for (String url : post.getImagenes()) {
                    ParseObject imageObject = new ParseObject("Image");
                    imageObject.put("url", url);
                    imageObject.saveInBackground(imgSaveError -> {
                        if (imgSaveError == null) {
                            // Después de guardar la imagen, agregamos la relación con el post
                            relation.add(imageObject);  // Relación entre Post e Image
                            // Guardar la relación del post con la imagen
                            postObject.saveInBackground(saveError -> {
                                if (saveError == null) {
                                    result.setValue("Post publicado");  // Publicación exitosa
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
}





