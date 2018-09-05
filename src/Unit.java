import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Unit extends JPanel implements Comparable<Unit> {
    private int id;
    private int px;
    private int py;
    private int dx;
    private int dy;

    public Unit(int px, int py, int dx, int dy, int id) {
        this.px = px;
        this.py = py;
        this.dx = dx;
        this.dy = dy;
        this.id = id;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public void setPy(int py) {
        this.py = py;
    }
    public int getPx() {
        return px;
    }

    public int getPy() {
        return py;
    }


    //	public int getX() {
    //		return px - 30;
    //	}
    //	
    //	public int getY() {
    //		return py - 30;
    //	}

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getId() {
        return id;
    }

    public void moveUnit() {
        int tempX = px;
        int tempY = py;
        px += dx;
        py += dy;
        clip(tempX, tempY);
    }

    private void clip(int tempX, int tempY) {
        if (tempX >= 420 && tempX <= 580) {
            py = Math.min(Math.max(py, 20), 700);
            if (tempY > 59 && tempY < 660) {
                px = Math.min(Math.max(px, 20), 980);
            } else {
                px = Math.min(Math.max(px, 420), 580);
            }
        } else {
            px = Math.min(Math.max(px, 20), 980);
            py = Math.min(Math.max(py, 60), 660);
        }
    }

    public int compareTo(Unit u) {
        return id - u.id;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(getClass() == o.getClass())) {
            return false;
        }
        Unit u = (Unit) o;
        return id == u.id;
    }

    public String toString() {
        return "" + id;
    }
}
