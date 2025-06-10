package application.model;

public class GameManager {
    private static final GameManager instance = new GameManager();

    //0 pour luffy, 1 pour nami et 2 pour zorro
    private static CharacterSelection selectedCharacter = new CharacterSelection(0,0,0);

    //nos map sont appelé map1,map2 etc...
    private int selectedMapIndex = 0;
    private int selectedDifficulty = 1;


    private GameManager() {}

    public static GameManager getInstance() {
        return instance;
    }

    public void setSelectedCharacter(CharacterSelection selection) {
        this.selectedCharacter = selection;
    }

    public static CharacterSelection getSelectedCharacter() {
        return selectedCharacter;
    }

    public int getSelectedMapIndex() {
        return selectedMapIndex;
    }
    public void setSelectedMapIndex(int selectedMapIndex) {
        this.selectedMapIndex = selectedMapIndex;
    }

    public String getSelectedMapImageName() {
        return "map" + (selectedMapIndex + 1) + ".png";
    }
    public void setSelectedDifficulty(int difficulty) {
        this.selectedDifficulty = difficulty;
    }
    public int getSelectedDifficulty() {
        return selectedDifficulty;
    }
}

