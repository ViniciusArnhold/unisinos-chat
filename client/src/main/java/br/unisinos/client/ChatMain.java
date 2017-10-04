package br.unisinos.client;

import br.unisinos.client.view.App;
import br.unisinos.commom.Constants;

public class ChatMain {

    static {
        System.setProperty(Constants.MAIN_CLASS_NAME_PROPERTY, ChatMain.class.getName());
    }

    public static void main(String[] args) {
        App.launch(App.class, args);
    }
}
