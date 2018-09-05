
public class LineSeg {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    public LineSeg(int x1, int y1, int x2, int y2) {
        startX = x1;
        startY = y1;
        endX = x2;
        endY = y2;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public boolean intersect(LineSeg line) {
        int x00 = startX;
        int y00 = startY;
        int x10 = line.startX;
        int y10 = line.startY;
        int x01 = endX - x00;
        int y01 = endY - y00;
        int x11 = line.endX - x10;
        int y11 = line.endY - y10;
        int d = x11 * y01 - x01 * y11;
        double t1 = (1.0 / d)  * ((x00 - x10) * y01 - (y00 - y10) * x01);
        double t2 = (1.0 / d) * ((x00 - x10) * y11 - (y00 - y10) * x11);
        return t1 >= 0 && t1 <= 1 && t2 >= 0 && t2 <= 1;
    }

    public boolean intersect(LineSeg[] lines) {
        double time = 1;
        boolean intersect = false;
        for (int i = 0; i < lines.length; i++) {
            int x00 = startX;
            int y00 = startY;
            int x10 = lines[i].startX;
            int y10 = lines[i].startY;
            int x01 = endX - x00;
            int y01 = endY - y00;
            int x11 = lines[i].endX - x10;
            int y11 = lines[i].endY - y10;
            int d = x11 * y01 - x01 * y11;
            double t1 = (1.0 / d)  * ((x00 - x10) * y01 - (y00 - y10) * x01);
            double t2 = (1.0 / d) * ((x00 - x10) * y11 - (y00 - y10) * x11);
            if (t1 >= 0 && t1 <= 1 && t2 >= 0 && t2 <= 1) {
                intersect = true;
                if (t2 < time) {
                    time = t2;
                }
            }
        }
        if (intersect) {
            endX = startX + (int) ((endX - startX) * time);
            endY = startY + (int) ((endY - startY) * time);
        }
        return intersect;
    }
}
