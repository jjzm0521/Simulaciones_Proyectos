package com.curso_simulaciones.mitrigesimaterceraapp.datos;

import java.io.File;

public interface IFolderItemListener {

    void OnCannotFileRead(File file);

    void OnFileClicked(File file);

}
