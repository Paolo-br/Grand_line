package application.model;

public class Island {
    private Position position;
    private boolean explored;
    private boolean visible;
    private boolean redLine;

    private boolean world;

    private boolean onePiece;

    private String view;

    private Boss boss;

    public Island(Position position, boolean explored,String view,boolean redLine,boolean world,boolean onePiece, boolean visible){
        this.position=position;
        this.explored=explored;
        this.view=view;
        this.redLine=redLine;
        this.world=world;
        this.onePiece=onePiece;
        this.visible=visible;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setexplored(boolean explored) {

        this.explored = explored;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public boolean isRedLine() {
        return redLine;
    }

    public void setRedLine(boolean redLine) {
        this.redLine = redLine;
    }

    public boolean isWorld() {
        return world;
    }

    public void setWorld(boolean world) {
        this.world = world;
    }

    public boolean isOnePiece() {
        return onePiece;
    }

    public void setOnePiece(boolean onePiece) {
        this.onePiece = onePiece;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }
}
