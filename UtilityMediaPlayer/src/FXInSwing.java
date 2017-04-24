import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class FXInSwing extends JFrame{

    JFXPanel panel;
    Scene scene;
    StackPane stack;
    Text hello;
    JFXPanel panel2;
    Scene scene2;
    StackPane stack2;
    Text hello2;
    

    boolean wait = true;

    public FXInSwing(){
        panel = new JFXPanel();
        panel2 = new JFXPanel();
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                stack = new StackPane();
                scene = new Scene(stack,300,300);
                hello = new Text("Hello");

                scene.setFill(Color.BLACK);
                hello.setFill(Color.WHEAT);
                hello.setEffect(new Reflection());

                panel.setScene(scene);
                stack.getChildren().add(hello);
                
                
                stack2 = new StackPane();
                scene2 = new Scene(stack2,300,300);
                hello2 = new Text("Hello");

                scene2.setFill(Color.RED);
                hello2.setFill(Color.BLACK);
                hello2.setEffect(new Reflection());

                panel2.setScene(scene2);
                stack2.getChildren().add(hello2);
                wait = false;
            }
        });
        
        this.getContentPane().add(panel2, BorderLayout.WEST);
        this.getContentPane().add(panel, BorderLayout.EAST);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 300);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                new FXInSwing();
            }
        });
    }
}  