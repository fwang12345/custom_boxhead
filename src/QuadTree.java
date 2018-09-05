import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class QuadTree {
    private int x;
    private int y;
    private int width;
    private int height;
    private int depth;
    private List<Vulnerable> units; 
    private QuadTree[] nodes;

    private static final int MAX_DEPTH = 5;
    private static final int MAX_UNITS = 4;

    public QuadTree(int x, int y, int width, int height, int depth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.depth = depth;
        units = new ArrayList<Vulnerable>();
        nodes = new QuadTree[4];
    }

    private int getQuadrant(int px, int py, double dx, double dy) {
        int halfW = width / 2;
        int halfH = height / 2;
        boolean top = py + dy <= y + halfH;
        boolean bottom = py - dy >= y + halfH;
        boolean left = px + dx <= x + halfW;
        boolean right = px - dx >= x + halfW;
        int num = -1;
        if (top) {
            if (left) {
                num = 1;
            } else if (right) {
                num = 0;
            } else {
                num = 4;
            }
        } else if (bottom) {
            if (left) {
                num = 2;
            } else if (right) {
                num = 3;
            } else {
                num = 6;
            }
        } else {
            if (left) {
                num = 5;
            } else if (right) {
                num = 7;
            } else {
                num = 8;
            }
        }
        return num;
    }

    private void divide() {
        int halfW = width / 2;
        int halfH = height / 2;
        nodes[0] = new QuadTree(x + halfW, y, width - halfW, halfH, depth + 1);
        nodes[1] = new QuadTree(x, y, halfW, halfH, depth + 1);
        nodes[2] = new QuadTree(x, y + halfW, halfW, height - halfH, depth + 1);
        nodes[3] = new QuadTree(x + halfW, y + halfH, width - halfW, halfH, depth + 1);
    }
    public void insert(Vulnerable unit) {
        int px = unit.getPx();
        int py = unit.getPy();

        if (nodes[0] != null) {
            int quad = getQuadrant(px, py, 20, 20);

            if (quad < 4) {
                nodes[quad].insert(unit);
                return;
            }
        }

        units.add(unit);

        if (units.size() > MAX_UNITS && depth < MAX_DEPTH && nodes[0] == null) {
            divide();
            int i = 0;
            while(i < units.size()) {
                Vulnerable uniti = units.get(i);
                int pxi = uniti.getPx();
                int pyi = uniti.getPy();
                int quad = getQuadrant(pxi, pyi, 20, 20);
                if (quad < 4) {
                    nodes[quad].insert(units.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    private static int clip(int x) {
        if (x != 0) {
            return x / Math.abs(x);
        } else {
            return x;
        }
    }

    private static boolean intersect(int px1, int py1, int px2, int py2) {
        return (px1 + 40 > px2 && py1 + 40 > py2
                && px2 + 40 > px1 && py2 + 40 > py1);
    }


    private void fixCollision(Vulnerable unit1, Vulnerable unit2) {
        int px1 = unit1.getPx();
        int py1 = unit1.getPy();
        int dx1 = clip(unit1.getDx());
        int dy1 = clip(unit1.getDy());
        int px2 = unit2.getPx();
        int py2 = unit2.getPy();
        int dx2 = clip(unit2.getDx());
        int dy2 = clip(unit2.getDy());
        if (intersect(px1, py1, px2, py2)) {
            if (unit1 instanceof Boxhead || unit2 instanceof Boxhead) {
                if (unit1 instanceof Zombie) {
                    Zombie z = (Zombie) unit1;
                    Boxhead b = (Boxhead) unit2;
                    z.damage(b);
                } else if (unit2 instanceof Zombie) {
                    Zombie z = (Zombie) unit2;
                    Boxhead b = (Boxhead) unit1;
                    z.damage(b);
                }
            }
            int xOverlap = 40 - Math.abs(px1 - px2);
            int yOverlap = 40 - Math.abs(py1 - py2);
            //			System.out.println("xOverlap: " + xOverlap + " yOverlap: " + yOverlap);
            //			System.out.println(dx1 + " " + dy1 + " " + dx2 + " " + dy2);
            if (dy1 == 0 && dy2 == 0) {
                if (dx1 == 0) {
                    unit2.setPx(px2 - dx2 * xOverlap);
                } else {
                    if (dx2 == 0) {
                        unit1.setPx(px1 - dx1 * xOverlap);
                    } else if (dx2 == -dx1) {
                        double decrement = xOverlap * unit1.getDx() / (unit1.getDx() - unit2.getDx());
                        unit1.setPx(px1 - (int) (dx1 * decrement));
                        unit2.setPx(px2 - (int) (dx2 * (xOverlap - decrement)));
                    } else {
                        if (dx1 < 0 == px1 < px2) {
                            unit2.setPx(px2 - dx2 * xOverlap);
                        } else {
                            unit1.setPx(px1 - dx1 * xOverlap);
                        }
                    }
                }
            } else if (dx1 == 0 && dx2 == 0) {
                if (dy1 == 0) {
                    unit2.setPy(py2 - dy2 * yOverlap);
                } else {
                    if (dy2 == 0) {
                        unit1.setPy(py1 - dy1 * yOverlap);
                    } else if (dy2 == -dy1) {
                        double decrement = yOverlap * unit1.getDy() / (unit1.getDy() - unit2.getDy());
                        unit1.setPy(py1 - (int) (dy1 * decrement));
                        unit2.setPy(py2 - (int) (dy2 * (yOverlap - decrement)));
                    } else {
                        if (dy1 < 0 == py1 < py2) {
                            unit2.setPy(py2 - dy2 * yOverlap);
                        } else {
                            unit1.setPy(py1 - dy1 * yOverlap);
                        }
                    }
                }
            } else {
                if (xOverlap <= yOverlap) {
                    if (dx1 == 0) {
                        unit2.setPx(px2 - dx2 * xOverlap);
                    } else {
                        unit1.setPx(px1 - dx1 * xOverlap);
                    }
                } else {
                    if (dy1 == 0) {
                        unit2.setPy(py2 - dy2 * yOverlap);
                    } else {
                        unit1.setPy(py1 - dy1 * yOverlap);
                    }
                }
            }
        }
    }

    private void addExtra(Vulnerable unit, List<List<Vulnerable>> listOfList) {
        int px = unit.getPx();
        int py = unit.getPy();
        int index = getQuadrant(px, py, 20, 20);
        if (index < 4) {
            listOfList.get(index).add(unit);
        } else if (index < 8){
            index -= 4;
            listOfList.get(index).add(unit);
            listOfList.get((index + 1) % 4).add(unit);
        } else {
            for (List<Vulnerable> list : listOfList) {
                list.add(unit);
            }
        }
    }
    private void detectCollisions(List<Vulnerable> extras) {
        for (int i = 0; i < units.size() - 1; i++) {
            for (int j = i + 1; j < units.size(); j++) {
                Vulnerable unit1 = units.get(i);
                Vulnerable unit2 = units.get(j);	
                //				System.out.println("Before");
                //				System.out.println("Id: " + unit1.getId() + " " + unit1.getPx() + " " + unit1.getPy());
                //				System.out.println("Id: " + unit2.getId() + " " + unit2.getPx() + " " + unit2.getPy());
                fixCollision(unit1, unit2);
                //				System.out.println("After");
                //				System.out.println("Id: " + unit1.getId() + " " + unit1.getPx() + " " + unit1.getPy());
                //				System.out.println("Id: " + unit2.getId() + " " + unit2.getPx() + " " + unit2.getPy());
            }
        }
        for (int i = 0; i < units.size(); i++) {
            for (int j = 0; j < extras.size(); j++) {
                Vulnerable unit1 = units.get(i);
                Vulnerable unit2 = extras.get(j);
                //				System.out.println("Before");
                //				System.out.println("Id: " + unit1.getId() + " " + unit1.getPx() + " " + unit1.getPy());
                //				System.out.println("Id: " + unit2.getId() + " " + unit2.getPx() + " " + unit2.getPy());
                fixCollision(unit1, unit2);
                //				System.out.println("After");
                //				System.out.println("Id: " + unit1.getId() + " " + unit1.getPx() + " " + unit1.getPy());
                //				System.out.println("Id: " + unit2.getId() + " " + unit2.getPx() + " " + unit2.getPy());
            }
        }

        //		for (int i = 0; i < extras.size() - 1; i++) {
        //			for (int j = i + 1; j < extras.size(); j++) {
        //				Vulnerable unit1 = extras.get(i);
        //				Vulnerable unit2 = extras.get(j);
        //				System.out.println("Before");
        //				System.out.println("Id: " + unit1.getId() + " " + unit1.getPx() + " " + unit1.getPy());
        //				System.out.println("Id: " + unit2.getId() + " " + unit2.getPx() + " " + unit2.getPy());
        //				fixCollision(unit1, unit2);
        //				System.out.println("After");
        //				System.out.println("Id: " + unit1.getId() + " " + unit1.getPx() + " " + unit1.getPy());
        //				System.out.println("Id: " + unit2.getId() + " " + unit2.getPx() + " " + unit2.getPy());
        //			}
        //		}
        if (nodes[0] != null) {
            List<Vulnerable> extras0 = new ArrayList<Vulnerable>();
            List<Vulnerable> extras1 = new ArrayList<Vulnerable>();
            List<Vulnerable> extras2 = new ArrayList<Vulnerable>();
            List<Vulnerable> extras3 = new ArrayList<Vulnerable>();
            List<List<Vulnerable>> listOfList = new ArrayList<List<Vulnerable>>();
            listOfList.add(extras0);
            listOfList.add(extras1);
            listOfList.add(extras2);
            listOfList.add(extras3);
            for (int i = 0; i < units.size(); i++) {
                Vulnerable unit = units.get(i);
                addExtra(unit, listOfList);
            }
            for (int i = 0; i < extras.size(); i++) {
                Vulnerable unit = extras.get(i);
                addExtra(unit, listOfList);
            }
            nodes[0].detectCollisions(extras0);
            nodes[1].detectCollisions(extras1);
            nodes[2].detectCollisions(extras2);
            nodes[3].detectCollisions(extras3);
        }
    }
    public void detectCollisions() {
        this.detectCollisions(new ArrayList<Vulnerable>());
    }

    public void recurse(int level, int quad) {
        System.out.println("Level: " + level +  " Quadrant: " + quad);
        for (int i = 0; i < units.size(); i++) {
            Vulnerable u = units.get(i);
            System.out.println(u.getPx() + " " + u.getPy());
        }
        if (nodes[0] != null) {
            nodes[0].recurse(level + 1, 0);
            nodes[1].recurse(level + 1, 1);
            nodes[2].recurse(level + 1, 2);
            nodes[3].recurse(level + 1, 3);
        }
    }

    private int getPointQuadrant(int px, int py) {
        boolean top = py < y + height / 2;
        boolean left = px < x + width / 2;
        if (top) {
            if (left) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (left) {
                return 2;
            } else {
                return 3;
            }
        }
    }
    private Set<Integer> getLineQuadrant(LineSeg line) {
        Set<Integer> index = new TreeSet<Integer>();
        int startX = line.getStartX();
        int startY = line.getStartY();
        int endX = line.getEndX();
        int endY = line.getEndY();
        int index1 = getPointQuadrant(startX, startY);
        int index2 = getPointQuadrant(endX, endY);
        index.add(index1);
        index.add(index2);
        if (Math.abs(index1 - index2) == 2) {
            int halfW = width / 2;
            int halfH = height / 2;
            LineSeg border1 = new LineSeg(x, y + halfH, x + halfW - 1, y + halfH);
            LineSeg border2 = new LineSeg(x + halfW + 1, y + halfH, x + width, y + halfH);
            boolean inter1 = line.intersect(border1);
            boolean inter2 = line.intersect(border2);
            if (index1 == 0 || index1 == 2) {
                if (inter1) {
                    index.add(1);
                } else if (inter2) {
                    index.add(3);
                }
            } else {
                if (inter1) {
                    index.add(2);
                } else if (inter2) {
                    index.add(0);
                }
            }
        }
        return index;

    }
    private boolean bulletIntersect(Vulnerable unit, LineSeg line) {
        int px = unit.getPx();
        int py = unit.getPy();
        LineSeg[] lines = new LineSeg[4];
        lines[0] = new LineSeg(px - 20, py - 20, px + 19, py - 20);
        lines[1] = new LineSeg(px + 19, py - 20, px + 19, py + 19);
        lines[2] = new LineSeg(px + 19, py + 19, px - 20, py + 19);
        lines[3] = new LineSeg(px - 20, py + 19, px - 20, py - 20);
        return line.intersect(lines[0]) || line.intersect(lines[1])
                || line.intersect(lines[2]) || line.intersect(lines[3]);
    }

    public void detectBulletCollision(List<Vulnerable> hit, LineSeg line) {
        for (Vulnerable u : units) {
            if (u instanceof Zombie || u instanceof Devil) {
                if (bulletIntersect(u, line)) {
                    hit.add((Vulnerable) u);
                }
            }
        }
        if (nodes[0] != null) {
            Set<Integer> index = getLineQuadrant(line);
            for (Integer i : index) {
                nodes[i].detectBulletCollision(hit, line);
            }
        }
    }


}
