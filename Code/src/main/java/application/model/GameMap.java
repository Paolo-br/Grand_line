package application.model;

import application.controller.BossController;
import application.controller.GameController;
import application.view.BossView;
import application.view.IslandView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente la carte du jeu, contenant les îles, les boss et leur affichage visuel.
 *
 * La carte est générée dynamiquement en fonction du niveau de difficulté.
 * Elle crée des îles fixes (ex : Red Line) et génère aléatoirement d'autres îles avec des boss.
 *
 * Ressources utilisées :
 * - JavaFX pour les vues (IslandView, BossView)
 * - Génération pseudo-aléatoire d’îles
 */
public class GameMap {

    /** Liste des îles générées sur la carte. */
    private final List<Island> iles = new ArrayList<>();

    /** Niveau de difficulté du jeu (1 = facile, 2 = moyen, 3 = difficile). */
    private final int difficulte;

    /** Liste des vues associées aux îles. */
    private final List<IslandView> islandViews = new ArrayList<>();

    /** Liste des vues associées aux boss. */
    private final List<BossView> bossViews = new ArrayList<>();

    /** Référence au contrôleur principal du jeu. */
    private final GameController gameController;

    /**
     * Constructeur de la carte de jeu.
     *
     * @param difficulte Niveau de difficulté (1, 2 ou 3).
     * @param gameController Contrôleur du jeu utilisé pour connecter les éléments.
     */
    public GameMap(int difficulte, GameController gameController) {
        this.difficulte = difficulte;
        this.gameController = gameController; // À l'avenir, la difficulté pourra être récupérée via la vue
        genererCarte();
    }

    /**
     * Retourne la liste des îles de la carte.
     *
     * @return Liste des objets Island.
     */
    public List<Island> getIles() {
        return iles;
    }

    /**
     * Retourne le niveau de difficulté actuel.
     *
     * @return Difficulté (1 à 3).
     */
    public int getDifficulte() {
        return difficulte;
    }

    /**
     * Génère la carte avec les îles principales et celles dépendant de la difficulté.
     * Ajoute également les vues et les boss associés.
     */
    private void genererCarte() {
        Island redlineTop = new Island(new Position(400, 0), false,"gand_linetop",true,false,false,true);
        iles.add(redlineTop);
        islandViews.add(new IslandView(redlineTop));

        Island redlineButtom = new Island(new Position(400, 500), false,"gand_linebuttom",true,false,false,true);
        iles.add(redlineButtom);
        islandViews.add(new IslandView(redlineButtom));

        Island worldRight = new Island(new Position(875, 200), false,"worldRight",false,true,false,true);
        iles.add(worldRight);
        islandViews.add(new IslandView(worldRight));

        Island worldLeft = new Island(new Position(0, 200), false,"worldLeft",false,true,false,true);
        iles.add(worldLeft);
        islandViews.add(new IslandView(worldLeft));

        Island ile1 = new Island(new Position(850, 50), false,"island19",false,false,false,false);
        Boss boss1 = new Boss("Mihawk", "boss19",1900);
        ile1.setBoss (boss1);
        BossView bossView1 = new BossView(boss1);
        bossView1.setLayoutX(850);
        bossView1.setLayoutY(10);
        bossViews.add(bossView1);
        iles.add(ile1);
        IslandView ileView1 =new IslandView(ile1);
        islandViews.add(ileView1);
        BossController bossController1 = new BossController(ile1, bossView1);
        bossController1.setGameController(gameController);
        ileView1.setBossView(bossView1);


        Island ile2 = new Island(new Position(850, 690), false,"island20",false,false,true,false);
        Boss boss2 = new Boss("Imu", "boss20",2000);
        ile2.setBoss (boss2);
        BossView bossView2 = new BossView(boss2);
        bossView2.setLayoutX(850);
        bossView2.setLayoutY(700);
        bossViews.add(bossView2);
        iles.add(ile2);
        IslandView ileView2 =new IslandView(ile2);
        islandViews.add(ileView2);
        BossController bossController2 = new BossController(ile2, bossView2);
        bossController2.setGameController(gameController);
        ileView2.setBossView(bossView2);


        Island ile3 = new Island(new Position(40.0, 30.0), false,"island9",false,false,false,false);
        Boss boss3=new Boss("Teach", "boss9",800);
        ile3.setBoss (boss3);
        BossView bossView3 = new BossView(boss3);
        bossView3.setLayoutX(40);
        bossView3.setLayoutY(10);
        bossViews.add(bossView3);
        iles.add(ile3);
        IslandView ileView3 =new IslandView(ile3);
        islandViews.add(ileView3);
        BossController bossController3 = new BossController(ile3, bossView3);
        bossController3.setGameController(gameController);
        ileView3.setBossView(bossView3);


        Island ile4 = new Island(new Position(40.0, 700), false,"island10",false,false,false,false);
        iles.add(ile4);
        Boss boss4= new Boss("Poisson", "boss10",1000);
        ile4.setBoss (boss4);
        BossView bossView4 = new BossView(boss4);
        bossView4.setLayoutX(40);
        bossView4.setLayoutY(660);
        bossViews.add(bossView4);
        iles.add(ile4);
        IslandView ileView4 =new IslandView(ile4);
        islandViews.add(ileView4);
        BossController bossController4 = new BossController(ile4, bossView4);
        bossController4.setGameController(gameController);
        ileView4.setBossView(bossView4);

        int totalIles = switch (difficulte) {
            case 1 -> 12;
            case 2 -> 16;
            case 3 -> 20;
            default -> 12; // Valeur par défaut
        };

        // On a déjà 4 îles principales fixes
        int ilesARajouter = (totalIles - 4)/2;

        // Générer les îles de droite
        ajouterIlesDansZone(ilesARajouter, 550, 810, 30, 740, 11);

        // Générer les îles de gauche
        ajouterIlesDansZone(ilesARajouter, 50, 330, 30, 740, 1);
    }

    /**
     * Génère un certain nombre d’îles dans une zone définie, avec évitement de collisions.
     *
     * @param nbIles   Nombre d'îles à ajouter.
     * @param xmin     Limite minimale X.
     * @param xmax     Limite maximale X.
     * @param ymin     Limite minimale Y.
     * @param ymax     Limite maximale Y.
     * @param numDebut Numéro de départ pour les noms/images.
     */
    private void ajouterIlesDansZone(int nbIles, double xmin, double xmax, double ymin, double ymax, int numDebut) {
        int ajoutees = 0;
        int essais = 0;
        int maxEssais = 20000;

        while (ajoutees < nbIles && essais < maxEssais) {
            essais++;
            double x = xmin + Math.random() * (xmax - xmin);
            double y = ymin + Math.random() * (ymax - ymin);
            Position position = new Position(x, y);

            boolean tropProche = isTropProche(x, y);

            if (!tropProche) {
                int index = numDebut + ajoutees;
                String nomImage = "island" + index;

                //Création automatique du boss
                String bossNom = "Boss " + index;
                String bossImage = "boss" + index;
                Boss boss = new Boss(bossNom, bossImage,index*80);
                BossView bossView = new BossView(boss);
                bossView.setLayoutX(position.getX());
                bossView.setLayoutY(position.getY() - 40);
                bossViews.add(bossView);
                Island ile = new Island(position, false, nomImage, false, false, false, false);
                if (index == 1){
                    ile.setVisible(true);
                }
                ile.setBoss(boss); // assigner le boss à l'île
                iles.add(ile);
                IslandView view = new IslandView(ile);
                islandViews.add(view);
                BossController bossController = new BossController(ile, bossView);
                bossController.setGameController(gameController);
                view.setBossView(bossView);
                ajoutees++;
            }
        }

        if (ajoutees < nbIles) {
            System.out.println("Seulement " + ajoutees + " îles placées (sur " + nbIles + ")");
        }
    }

    /**
     * Vérifie si une position est trop proche d’une île déjà existante ou de la zone de départ.
     *
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @return true si la position est trop proche, false sinon.
     */
    private boolean isTropProche(double x, double y) {
        boolean tropProche = false;

        // Vérifier proximité avec les autres îles déjà placées
        for (Island autre : iles) {
            double dx = autre.getPosition().getX() - x;
            double dy = autre.getPosition().getY() - y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < 130) {
                tropProche = true;
                break;
            }
        }

        // Vérifier proximité avec la zone du bateau de départ
        double dxStart = 53 - x;
        double dyStart = 346 - y;
        double distToStart = Math.sqrt(dxStart * dxStart + dyStart * dyStart);
        if (distToStart < 200) {
            tropProche = true;
        }
        return tropProche;
    }

    /**
     * Retourne la liste des vues des îles.
     *
     * @return Liste des objets IslandView.
     */
    public List<IslandView> getIslandViews() {
        return islandViews;
    }

    /**
     * Retourne la liste des vues des boss.
     *
     * @return Liste des objets BossView.
     */
    public List<BossView> getBossViews() {
        return bossViews;
    }

    /**
     * Récupère l'île contenant le One Piece (marquée comme telle).
     *
     * @return L'île contenant le One Piece, ou null si non trouvée.
     */
    public Island getOnepiece() {
        for (Island end : iles) {
            if (end.isOnePiece()) {
                return end;
            }
        }
        return null;
    }
}
