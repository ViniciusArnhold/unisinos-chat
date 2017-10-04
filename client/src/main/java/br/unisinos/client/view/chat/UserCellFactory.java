package br.unisinos.client.view.chat;

import br.unisinos.commom.User;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class UserCellFactory implements Callback<ListView<User>, ListCell<User>> {
    /**
     * The <code>call</code> method is called when required, and is given a
     * single argument of type P, with a requirement that an object of type R
     * is returned.
     *
     * @param param The single argument upon which the returned value should be
     *              determined.
     * @return An object of type R that may be determined based on the provided
     * parameter value.
     */
    @Override
    public ListCell<User> call(ListView<User> param) {
        return new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setGraphic(null);
                setText(null);

                if (user != null) {
                    Text name = new Text(user.getName());
                    Image image = new Image(getClass().getClassLoader().getResourceAsStream("image/annon-user.png"), 50, 50, true, true);
                    ImageView imageView = new ImageView(image);
                    imageView.setImage(image);

                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(imageView, name);
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(hBox);
                }
            }
        };
    }
}
