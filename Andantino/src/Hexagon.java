import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon
{
    private Pair pair;

    public Hexagon(double x, double y)
    {
        this.getPoints().addAll(x, y + GUI.length,
                x + (GUI.length * GUI.v), y + GUI.length / 2,
                x + (GUI.length * GUI.v), y - GUI.length / 2,
                x, y - GUI.length,
                x - (GUI.length * GUI.v), y - GUI.length / 2,
                x - (GUI.length * GUI.v), y + GUI.length / 2);
        this.setStrokeWidth(2);
        this.setStroke(Paint.valueOf("#000000"));
        this.setFill(Paint.valueOf("#D2691E"));
    }

    public void unfill() {this.setFill(Paint.valueOf("#D2691E"));}

    public void setFill(boolean colour)
    {
        if(colour) {setFill(Paint.valueOf("#FFFFFF"));}
        else {setFill(Paint.valueOf("#000000"));}
    }

    public int getRow() {return pair.getX();}

    public int getCol() {return pair.getY();}
}