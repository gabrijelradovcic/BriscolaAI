
import GameEngine.Card;
import Players.*;
import javafx.collections.FXCollections;
import javafx.scene.*;
import javafx.scene.control.*;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import NeuroEvolution.Game.GameState;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.animation.TranslateTransition;

import GameEngine.*;

public class GUI extends Application {

    Label points1, points2, playerName1, playerName2, result, whosTurn ;

    Double startX, startY;
    Double cardWidth = 63 * 1.5, cardHeight = 88 * 1.5;
    Color tableColor = Color.rgb(0, 66, 37);
    boolean playLeft = true;
    int countLeft1 = 0;
    int countLeft2 = 0;

    boolean player1isBot = false;
    boolean player2isBot = false;
    ArrayList<ImageView> ImageViewList = new ArrayList<ImageView>();
    ArrayList<Rectangle> cards = new ArrayList<Rectangle>();
    ArrayList<ImageView> cardPlayer1 = new ArrayList<ImageView>();
    ArrayList<ImageView> cardPlayer2 = new ArrayList<ImageView>();
    String cardPictures[] = new String[41];
    // check num of left card in hand
    int checkCoverNum = 6;
    boolean[] checkCoverCondition1 = new boolean[3];
    boolean[] checkCoverCondition2 = new boolean[3];

    int numberOfCards = 0;
    boolean briskoladeck = false;
    Briscola briscolaGame;
    Deck deck;
    Player player1;
    Player player2;
    boolean twoCardsTrown = false;
    boolean player1isonturn = true;
    boolean firstRound = true;
    ImageView[] lastTwoCards = new ImageView[2];
    ImageView[] coverOpp = new ImageView[3];
    Point2D[] theirLocations = new Point2D[2];
    Group root = new Group();
    ImageView cardback1, cardback2, cardback, briscola, temp100;
    String streamCardBack;
    Button help;
    ChoiceBox choosePlayer1, choosePlayer2;
    Stage stage;

    int BriscolaSuit;
    int[] usedCardnumbers = new int[40];
    int[] usedCardsuits = new int[40];

    public static void main(String args[]) {

        launch(args);
    }

    public void visitAllCards(String startName) throws IOException {
        Path start = Paths.get(startName);
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                cardPictures[numberOfCards] = file.toString();
                numberOfCards++;
                return FileVisitResult.CONTINUE;
            }
        });

    }

    public void start(Stage stage) throws IOException {
        this.stage = stage;
        Label name = new Label("briscola");
        name.setTextFill(Color.WHITE);
        name.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 50));
        VBox layout0 = new VBox(20);
        Scene scene0 = new Scene(layout0, 500, 500);
        TextArea area1 = new TextArea("Player1");
        TextArea area2 = new TextArea("Player2");
        area1.setMaxSize(120,5);
        area2.setMaxSize(120,5);
        area1.setTranslateX(1);
        area2.setTranslateX(1);
        Label input = new Label("Please enter player names:");
        Label choosePlayers = new Label("Select human or bot player:");
        input.setTextFill(Color.WHITE);
        choosePlayers.setTextFill(Color.WHITE);

        input.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20));
        choosePlayers.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20));

        whosTurn = new Label("Your turn:");
        whosTurn.setTextFill(Color.WHITE);
        whosTurn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20));
        whosTurn.relocate(150, 20);

        points1 = new Label("points: 0");
        points2 = new Label("points: 0");
        help = new Button("How to play");
        help.setPrefSize(100, 35);

        Button gameHistory = new Button("Game history");
        gameHistory.setPrefSize(100, 35);

        layout0.getChildren().addAll(name, input, area1, area2);
        Button start = new Button("Start!");
        start.setPrefSize(100, 35);
        start.setTranslateX(200);
        help.setTranslateX(200);
        gameHistory.setTranslateX(200);
        start.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20));
        gameHistory.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));
        help.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 13));

        layout0.setSpacing(14);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How to play");
        alert.setHeaderText("Game rules");
        alert.setContentText(
                "By means of a 40 card deck, the object of the game is to take cards which gives you a high score the point value is as follows: \n"
                        +
                        "        Ace\t11: points, Three: 10 points, King: 4 points, Queen: 3 points, Jack:2 points and the rest have no points. \n"
                        +
                        "        One of the two players shuffles the deck and deals three cards to each player. \n" +
                        "        He then takes the next card and puts it face up near the pile of undealt cards, which are placed face down. \n"
                        +
                        "        The face-up card suit defines which will be the Briscola suit for the game, i.e. the suit which always takes all other cards, card ranking notwithstanding. \n"
                        +
                        "        The game starts. The first to play is the non-dealer. \n" +
                        "            A leads one of his three cards, face up. \n" +
                        "            B plays one of his cards, and wins or loses the trick according to these three simple rules: \n"
                        +
                        "        \n" +
                        "        1) If B plays a card of the same suit as the card led by A, then the trick is won by whoever played the higher card - the winner takes both cards away, and puts them, face down, in a pile near him. \n"
                        +
                        "        2) If B plays a card which has a different suit from the card which A led, but neither card is a Briscola (trump), A wins the trick, and the cards will go to A, even if B's card was of higher rank. \n"
                        +
                        "        3) If B plays a card of a different suit from A's, and one of the cards is a Briscola (trump), then the player of the Briscola wins the trick.\n");
        help.setOnAction(e -> alert.show());
        choosePlayer1 = new ChoiceBox(FXCollections.observableArrayList(
                "Human", "NeuralNetwork","MonteCarlo","QPlayer","GreedyPlayer","RandomPlayer","HybridPlayer"));
        choosePlayer1.setValue("Human");
        System.out.println(choosePlayer1.getValue().toString());
        choosePlayer2 = new ChoiceBox(FXCollections.observableArrayList(
                "Human","NeuralNetwork", "MonteCarlo","QPlayer","GreedyPlayer","RandomPlayer","HybridPlayer"));
        choosePlayer2.setValue("Neural Network");

        String finalData = "";
   /*     try {
            File myObj = new File("gamehistory.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data+='\n';
                finalData += data;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

*/
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("Game History");
        alert2.setHeaderText("Game History");
        alert2.setContentText(finalData);

        gameHistory.setOnAction(e -> alert2.show());

        start.setOnAction(e -> {
            if (area1.getText().equals(""))
                playerName1.setText("Player1");
            else
                playerName1.setText(area1.getText());
            if (area2.getText().equals(""))
                playerName2.setText("Player2");
            else
                playerName2.setText(area2.getText());
            Scene scene = new Scene(root, 800, 750, tableColor);
            stage.setScene(scene);
            // scene.setOnMouseClicked(ee ->{
            // reSetCover();
            // });
            Player temp1 = getPlayer(choosePlayer1.getValue().toString());
            Player temp2 = getPlayer(choosePlayer2.getValue().toString());
            briscolaGame = new Briscola(temp1, temp2);
            deck = briscolaGame.getDeck();
            player1 = briscolaGame.getPlayer1();
            player2 = briscolaGame.getPlayer2();
            player1isBot = isBot(player1.getName());
            player2isBot = isBot(player2.getName());
            try {
                startGame();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        playerName1 = new Label(area1.getText());
        playerName2 = new Label(area2.getText());
        playerName1.setTextFill(Color.WHITE);
        playerName2.setTextFill(Color.WHITE);
        playerName1.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20));
        playerName2.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20));
        playerName1.setTranslateX(1);
        playerName1.setTranslateY(1);
        playerName2.setTranslateX(1);
        playerName2.setTranslateY(680);

        points1 = new Label("points: 0");
        points2 = new Label("points: 0");
        points1.setTextFill(Color.WHITE);
        points2.setTextFill(Color.WHITE);
        points1.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20));
        points2.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20));
        points1.setTranslateX(1);
        points1.setTranslateY(25);
        points2.setTranslateX(1);
        points2.setTranslateY(700);

        layout0.getChildren().addAll(choosePlayers,choosePlayer1,choosePlayer2,start,help,gameHistory);
        layout0.setBackground(Background.fill(tableColor));

        // Setting the Scene object
        stage.setTitle("Briscola demo");
        stage.setScene(scene0);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.exit(0);
            }
        });
    }

    public void startGame() throws IOException {
        if (briskoladeck) {
            visitAllCards("cards01");
        } else {
            visitAllCards("PNG-cards-1.3");
        }

        /* test for mac

          String temp = new String ();
         temp = cardPictures[40];
          cardPictures[40] = cardPictures[7];
          cardPictures[7] = temp;
*/

        checkCoverCondition1[0] = true;
        checkCoverCondition1[1] = true;
        checkCoverCondition1[2] = true;
        checkCoverCondition2[0] = true;
        checkCoverCondition2[1] = true;
        checkCoverCondition2[2] = true;

        streamCardBack = cardPictures[40];
        FileInputStream stream = new FileInputStream(streamCardBack);
        cardback = setImage(stream);
        cardback1 = setImage(stream);
        cardback2 = setImage(stream);
        temp100 = setImage(stream);
        FileInputStream cover01 = new FileInputStream(cardPictures[40]);
        FileInputStream cover02 = new FileInputStream(cardPictures[40]);
        FileInputStream cover03 = new FileInputStream(cardPictures[40]);
        coverOpp[0] = setImage(cover01);
        coverOpp[1] = setImage(cover02);
        coverOpp[2] = setImage(cover03);

        SpotLight light = new SpotLight();
        light.setColor(Color.PURPLE);
        light.relocate(300, 300);
        light.setRotate(45);

        cardback.relocate(100, 300);
        cardback1.relocate(20, 20);
        cardback2.relocate(700, 600);

        for (int j = 0; j < 40; j++) {
            ImageViewList.add(setImage(new FileInputStream(cardPictures[j])));
        }

        briscola = ImageViewList.get(briscolaGame.briscola.getPositionOfImage()); // briscola card
        briscola.relocate(150, 300);
        briscola.setRotate(90);
        HBox p1 = new HBox(3);
        HBox p2 = new HBox(3);

        HBox pile = new HBox(40);

        pile.setSpacing(-60);
        pile.setScaleX(cardWidth);
        pile.setScaleY(cardHeight);
        ImageView p1card1 = ImageViewList.get(player1.getCard(0).getPositionOfImage());
        ImageView p1card2 = ImageViewList.get(player1.getCard(1).getPositionOfImage());
        ImageView p1card3 = ImageViewList.get(player1.getCard(2).getPositionOfImage());
        ImageView p2card1 = ImageViewList.get(player2.getCard(0).getPositionOfImage());
        ImageView p2card2 = ImageViewList.get(player2.getCard(1).getPositionOfImage());
        ImageView p2card3 = ImageViewList.get(player2.getCard(2).getPositionOfImage());

        HBox take = new HBox(3);
        Button takePot = new Button("take pot");
        takePot.setPrefSize(110, 40);
        takePot.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));

        take.getChildren().add(takePot);
        take.getChildren().get(0).relocate(650, 350);

        p1.getChildren().add(p1card1);
        p1.getChildren().add(p1card2);
        p1.getChildren().add(p1card3);
        p2.getChildren().add(p2card1);
        p2.getChildren().add(p2card2);
        p2.getChildren().add(p2card3);

        p1.getChildren().get(0).relocate(300, 20);
        p1.getChildren().get(1).relocate(400, 20);
        p1.getChildren().get(2).relocate(500, 20);
        p2.getChildren().get(2).relocate(300, 600);
        p2.getChildren().get(1).relocate(400, 600);
        p2.getChildren().get(0).relocate(500, 600);

        root.getChildren().addAll(p1.getChildren().get(0), p1.getChildren().get(1), p1.getChildren().get(2));
        //root.getChildren().forEach(this::makeForPlayer1Clickable);
        root.getChildren().addAll(p2.getChildren().get(0), p2.getChildren().get(1), p2.getChildren().get(2),
                take.getChildren().get(0));
        if (!player2isBot) {
            makeForPlayer2Clickable(root.getChildren().get(3));
            makeForPlayer2Clickable(root.getChildren().get(4));
            makeForPlayer2Clickable(root.getChildren().get(5));
        } else {
            // make a botThread
            BotThread botThread = new BotThread(false, player2, player1);
            Thread thread = new Thread(botThread);
            thread.start();
        }
        if (!player1isBot) {
            makeForPlayer1Clickable(root.getChildren().get(0));
            makeForPlayer1Clickable(root.getChildren().get(1));
            makeForPlayer1Clickable(root.getChildren().get(2));
        } else {
            // make a botThread
            BotThread2 botThread2 = new BotThread2(true, player1, player2);
            Thread thread = new Thread(botThread2);
            thread.start();
        }
        takePotListener(root.getChildren().get(6));
        root.getChildren().addAll(cardback1, cardback2, briscola, cardback, points1, points2, playerName1, playerName2,
                whosTurn, coverOpp[0], coverOpp[1], coverOpp[2]);
        // root.getChildren().set(100,temp100);
        if(!player1isBot){
            coverOpp[0].relocate(300, 600);
            coverOpp[1].relocate(400, 600);
            coverOpp[2].relocate(500, 600);
        }
        else{
            coverOpp[0].relocate(300, 20);
            coverOpp[1].relocate(400, 20);
            coverOpp[2].relocate(500, 20);
        }
        pile.setBackground(Background.fill(tableColor));
    }

    private void makeMove(boolean player, int index) {
        if (player) {
            if (!twoCardsTrown) {
                player1.removeCard(index);
                if (playLeft) {
                    playLeft = false;
                } else {
                    playLeft = true;
                    twoCardsTrown = true;
                }
                player1isonturn = false;
            }
            player1isonturn = false;
        } else {
            if (!twoCardsTrown) {
                player2.removeCard(index);
                if (playLeft) {
                    playLeft = false;
                } else {
                    playLeft = true;
                    twoCardsTrown = true;
                }

            }
            player1isonturn = true;
        }

    }

    public void handleAnimation(Node node) {
        if (twoCardsTrown) {
            return;
        }
        if (playLeft) {
            lastTwoCards[0] = (ImageView) node;
            Point2D point = node.localToScene(0.0, 0.0);
            theirLocations[0] = point;
            movement(node, 400, 300);
        } else {
            lastTwoCards[1] = (ImageView) node;
            Point2D point = node.localToScene(0.0, 0.0);
            theirLocations[1] = point;
            movement(node, 450, 300);
        }
        // node.toFront();

    }

    private void makeForPlayer1Clickable(Node node) {
        node.setOnMouseClicked(e -> {
            if (player1isonturn) {
                handleAnimation(node);
                Point2D point = node.localToScene(0.0, 0.0);
                makeMove(true, checkIndex(point));
                moverCover(true, checkIndex(point));
            }

        });

    }

    private void makeForPlayer2Clickable(Node node) {
        node.setOnMouseClicked(e -> {
            if (!player1isonturn) {
                handleAnimation(node);
                Point2D point = node.localToScene(0.0, 0.0);
                makeMove(false, checkIndex(point));
                moverCover(false, checkIndex(point));
            }

        });

    }
    private void removeBotCard(int move){
        if(player1isBot)
            coverOpp[move].relocate(-100, 0);
        else{
            coverOpp[2-move].relocate(-100, 0);
        }
    }

    String list = "";
    private void takePotListener(Node node) {
        if (!deck.isEmpty()) {
            reSetCover();
            checkCoverNum = 3;
        }
        node.setOnMouseClicked(e -> {
            if (twoCardsTrown) {
                lastTwoCards[0].setImage(null);

                Player winner = briscolaGame.checkWin(player1.getLastTrownCard(), player2.getLastTrownCard());
                winner.addPoints(player1.getLastTrownCard().pointValue() + player2.getLastTrownCard().pointValue());
                twoCardsTrown = false;
                points1.setText("points: " + player1.getPoints()); // update points
                points2.setText("points: " + player2.getPoints());
                if (!deck.isEmpty()) {
                    player1isonturn = briscolaGame.firstIsOnTurn;
                    lastTwoCards[0].relocate(theirLocations[0].getX(), theirLocations[0].getY());
                    lastTwoCards[1].relocate(theirLocations[1].getX(), theirLocations[1].getY());

                    if (player1isonturn) {
                        whosTurn.relocate(150, 20);
                        try {
                            collectAnimiation(1);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (theirLocations[0].getY() < theirLocations[1].getY()) {
                            Card p1card = deck.deal();
                            player1.addCard(p1card);
                            Image image1 = new Image(cardPictures[p1card.getPositionOfImage()]);
                            lastTwoCards[0].setImage(image1);
                            lastTwoCards[0].setTranslateY(0); // don't know why but with this (0) execution it works
                            lastTwoCards[0].setTranslateX(0);

                            Card p2card = deck.deal();
                            player2.addCard(p2card);
                            Image image2 = new Image(cardPictures[p2card.getPositionOfImage()]);
                            lastTwoCards[1].setImage(image2);
                            lastTwoCards[1].setTranslateY(0);
                            lastTwoCards[1].setTranslateX(0);

                        } else {
                            Card p1card = deck.deal();
                            player1.addCard(p1card);
                            Image image1 = new Image(cardPictures[p1card.getPositionOfImage()]);
                            lastTwoCards[1].setImage(image1);
                            lastTwoCards[1].setTranslateY(0);
                            lastTwoCards[1].setTranslateX(0);
                            Card p2card = deck.deal();
                            player2.addCard(p2card);
                            Image image2 = new Image(cardPictures[p2card.getPositionOfImage()]);
                            lastTwoCards[0].setImage(image2);
                            lastTwoCards[0].setTranslateY(0);
                            lastTwoCards[0].setTranslateX(0);

                        }
                    } else {
                        whosTurn.relocate(150, 600);
                        try {
                            collectAnimiation(2);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (theirLocations[0].getY() < theirLocations[1].getY()) {
                            Card p2card = deck.deal();
                            player2.addCard(p2card);
                            Image image2 = new Image(cardPictures[p2card.getPositionOfImage()]);
                            lastTwoCards[1].setImage(image2);
                            lastTwoCards[1].setTranslateY(0);
                            lastTwoCards[1].setTranslateX(0);
                            Card p1card = deck.deal();
                            player1.addCard(p1card);
                            Image image1 = new Image(cardPictures[p1card.getPositionOfImage()]);
                            lastTwoCards[0].setImage(image1);
                            lastTwoCards[0].setTranslateY(0);
                            lastTwoCards[0].setTranslateX(0);
                        } else {
                            Card p2card = deck.deal();
                            player2.addCard(p2card);
                            Image image2 = new Image(cardPictures[p2card.getPositionOfImage()]);
                            lastTwoCards[0].setImage(image2);
                            lastTwoCards[0].setTranslateY(0);
                            lastTwoCards[0].setTranslateX(0);
                            Card p1card = deck.deal();
                            player1.addCard(p1card);
                            Image image1 = new Image(cardPictures[p1card.getPositionOfImage()]);
                            lastTwoCards[1].setImage(image1);
                            lastTwoCards[1].setTranslateY(0);
                            lastTwoCards[1].setTranslateX(0);
                        }
                    }
                    if (deck.isEmpty()) {
                        root.getChildren().removeAll(cardback, briscola);
                    }
                } else {
                    if (player1.isHandEmpty() && player2.isHandEmpty()) {
                        lastTwoCards[0].setImage(null); // simple way to remove current card.
                        lastTwoCards[1].setImage(null);
                        VBox layout3 = new VBox();
                        Color tableColor = Color.rgb(0, 66, 37);
                        layout3.setBackground(Background.fill(tableColor));
                        Scene resultScene = new Scene(layout3, 500, 500);
                        stage.setScene(resultScene);
                        result = new Label(briscolaGame.getWinner() + " Wins!");
                        Label playerNameLabel1 = new Label(player1.getName() + " points: " + player1.getPoints());
                        Label playerNameLabel2 = new Label(player2.getName() + " points: " + player2.getPoints());
                        list += player1.getName()+ " " + player1.getPoints() + " - " + player2.getPoints() + " " + player2.getName() + " (" + briscolaGame.getWinner() + " Wins!" + ")\n";
                        try {
                            Files.write(Paths.get("gamehistory.txt"), list.getBytes(), StandardOpenOption.APPEND);
                        } catch (IOException e1) {
                            System.out.println("An error occurred.");
                            e1.printStackTrace();
                        }
                        playerNameLabel1.setTextFill(Color.WHITE);
                        playerNameLabel2.setTextFill(Color.WHITE);
                        playerNameLabel1.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20));
                        playerNameLabel2.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20));
                        result.setTextFill(Color.WHITE);
                        result.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 60));
                        result.setTranslateX(100);
                        result.setTranslateY(100);
                        result.setTranslateX(50);
                        result.setTranslateY(200);
                        layout3.getChildren().addAll(result, playerNameLabel1, playerNameLabel2);
                        playerNameLabel1.setTranslateX(50);
                        playerNameLabel2.setTranslateX(50);

                    } else {
                        player1isonturn = briscolaGame.firstIsOnTurn;
                        lastTwoCards[0].setImage(null);
                        lastTwoCards[1].setImage(null);

                        if (player1isonturn) {

                            whosTurn.relocate(150, 20);
                            try {
                                collectAnimiation(1);
                            } catch (FileNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {

                            whosTurn.relocate(150, 600);
                            try {
                                collectAnimiation(2);
                            } catch (FileNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    }
                }
            }
        });
        if (!deck.isEmpty()) {
            reSetCover();
            checkCoverNum = 3;
        }

    }

    public void collectAnimiation(int winnerIndex) throws FileNotFoundException {
        FileInputStream back = new FileInputStream(cardPictures[40]);
        FileInputStream back2 = new FileInputStream(cardPictures[40]);
        ImageView img = setImage(back);
        ImageView img2 = setImage(back2);
        root.getChildren().addAll(img, img2);
        img.relocate(400, 300);
        img2.relocate(450, 300);
        if (winnerIndex == 1) {
            movement(img, 700, 10);
            movement(img2, 700, 10);
        } else {
            movement(img, 700, 600);
            movement(img2, 700, 600);
        }
        if (!deck.isEmpty()) {
            reSetCover();
            checkCoverNum = 3;

        }
    }

    public void dealAnimation(Node node, double x, double y) throws FileNotFoundException {
        // FileInputStream back = new FileInputStream(cardPictures[7]);
        // ImageView img = setImage(back);
        // root.getChildren().addAll(img);
        node.relocate(100, 300);
        movement(node, x, y);
        root.getChildren().remove(node);

    }

    public int checkIndex(Point2D point) {
        double x = point.getX();
        if ((int) x <= 300) {
            return 0;
        } else if ((int) x <= 400) {
            return 1;
        } else {
            return 2;
        }
    }

    /*
     * private void makeDraggable (Node node){
     * node.setOnMousePressed(e -> {
     * startX = e.getSceneX() - node.getTranslateX();
     * startY = e.getSceneY() - node.getTranslateY();
     * });
     * node.setOnMouseDragged(e -> {
     * node.setTranslateX(e.getSceneX() - startX);
     * node.setTranslateY(e.getSceneY() - startY);
     *
     * });
     * }
     *
     */


    public ImageView setImage(InputStream IS) {
        Image image = new Image(IS);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(cardWidth);
        imageView.setFitHeight(cardHeight);
        return imageView;
    }

    public void movement(Node node, double newX, double newY) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        double X = node.getLayoutX();
        double Y = node.getLayoutY();
        translate.setByX(newX - X);
        translate.setByY(newY - Y);
        // translate.setCycleCount(500);
        // translate.setAutoReverse(true);
        translate.setNode(node);
        translate.play();

        /*
         * TranslateTransition translate = new TranslateTransition();
         * translate.setByX(-400);
         * translate.setByY(-400);
         *
         * translate.setCycleCount(500);
         * translate.setAutoReverse(false);
         * translate.setNode(takePot);
         * translate.play();
         */

    }

    public Player getPlayer(String name) {
        if (name.equals("NeuralNetwork")) {

            return new NeuralPlayer();
        }
        if(name.equals("MonteCarlo")){
            return new MonteCarloPlayer();
        }
        if(name.equals("QPlayer")){
            return new QPlayer();
        }
        if(name.equals("GreedyPlayer")){
            return new GreedyPlayer();
        }
        if(name.equals("RandomPlayer")){
            return new RandomPlayer();
        }
        if(name.equals("HybridPlayer")){
            return new HybridPlayer();
        }
        else {
            return new Player(name);
        }
    }

    public boolean isBot(String name) {
        if (name.equals("NeuralPlayer")) {

            return true;
        }
        if(name.equals("MonteCarloPlayer")){
            return true;
        }if(name.equals("QPlayer")){
            return true;
        }
        if(name.equals("GreedyPlayer")){
            return true;
        }
        if(name.equals("RandomPlayer")){
            return true;
        }
        if(name.equals("HybridPlayer")){
            return true;
        }else {
            return false;
        }
    }

    // Thread that continously checks if the bot is on turn
    // takes a variable that is true if the player is the first player
    // and false if the player is the second player
    // and takes the player object
    public class BotThread implements Runnable {
        private boolean turn;
        private Player p;
        private Player opp;

        public BotThread(boolean turn, Player p, Player opp) {
            this.turn = turn;
            this.p = p;
            this.opp = opp;
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                }
                if (!player2.isHandEmpty()&&turn == player1isonturn && !twoCardsTrown) {
                    GameState game = getGameState(p, opp);
                    int move = p.getDecision(game,briscolaGame);
                    Node n = getNodeFromInt(move, turn); ///////////////////////
                    System.out.println(Arrays.toString(p.hand));
                    handleAnimation(n);
                    makeMove(turn, move);
                    removeBotCard(move);

                }
            }
        }
    }
    public class BotThread2 implements Runnable {
        private boolean turn;
        private Player p;
        private Player opp;

        public BotThread2(boolean turn, Player p, Player opp) {
            this.turn = turn;
            this.p = p;
            this.opp = opp;
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                }
                if (!player1.isHandEmpty()&&turn == player1isonturn && !twoCardsTrown) {
                    GameState game = getGameState(p, opp);
                    int move = p.getDecision(game,briscolaGame);
                    Node n = getNodeFromInt(move, turn); ///////////////////////
                    handleAnimation(n);
                    makeMove(turn, move);
                    removeBotCard(move);

                }
            }
        }
    }

    int counter = 0;

    public void recordCard(Card c) {
        usedCardnumbers[counter] = RankToInt(c.rank());
        usedCardsuits[counter] = SuitToInt(c.suit());
        counter++;
    }

    public GameState getGameState(Player player1, Player player2) {
        int yourScore = player1.getPoints();
        int oppScore = player2.getPoints();
        int yourCards[] = new int[3];
        int yourCardsuits[] = new int[3];
        for (int i = 0; i < 3; i++) {
            if (player1.getCard(i) != null) {
                yourCards[i] = RankToInt(player1.getCard(i).rank());
                yourCardsuits[i] = SuitToInt(player1.getCard(i).suit());
            } else {
                yourCards[i] = 0;
                yourCardsuits[i] = 0;
            }
        }
        int briscola = SuitToInt(briscolaGame.briscola.suit());
        GameState gs = new GameState(yourScore, oppScore, briscola, usedCardnumbers, usedCardsuits, yourCards,
                yourCardsuits);
        return gs;

    }

    public int RankToInt(String rank) {
        if (rank.equals("Ace")) {
            return 1;
        } else if (rank.equals("2")) {
            return 2;
        } else if (rank.equals("3")) {
            return 3;
        } else if (rank.equals("4")) {
            return 4;
        } else if (rank.equals("5")) {
            return 5;
        } else if (rank.equals("6")) {
            return 6;
        } else if (rank.equals("7")) {
            return 7;
        } else if (rank.equals("8")) {
            return 8;
        } else if (rank.equals("9")) {
            return 9;
        } else if (rank.equals("10")) {
            return 10;
        } else if (rank.equals("Jack")) {
            return 11;
        } else if (rank.equals("Queen")) {
            return 12;
        } else if (rank.equals("King")) {
            return 13;
        } else {
            return 0;
        }

    }

    public int SuitToInt(String suit) {
        if (suit.equals("Spades")) {
            return 1;
        } else if (suit.equals("Hearts")) {
            return 2;
        } else if (suit.equals("Diamonds")) {
            return 3;
        } else {
            return 4;
        }
    }

    int temp = 0;

    // when a bot gives a move we need to know which node is associated with that
    // card
    // this method takes in the card and which player makes the move
    // and returns the node that is associated with that card
    public Node getNodeFromInt(int i, Boolean player1) {

        if (!deck.isEmpty()) {
            if (player1) {
                return root.getChildren().get(i);
            } else {
                return root.getChildren().get(i + 3);
            }
        } else {
            if (player1) {
                countLeft1++;

                return root.getChildren().get(countLeft1 - 1);

            } else {
                countLeft2++;

                return root.getChildren().get(countLeft2 - 1 + 3);

            }
        }
        // i can be either 0 1 2
    }

    public void reSetCover() {
        if (!player1isBot && !player2isBot) {
            checkCoverCondition1[0] = true;
            checkCoverCondition1[1] = true;
            checkCoverCondition1[2] = true;
            checkCoverCondition2[0] = true;
            checkCoverCondition2[1] = true;
            checkCoverCondition2[2] = true;

            if (player1isonturn) {
                whosTurn.relocate(150, 20);

                coverOpp[0].relocate(300, 600);
                coverOpp[1].relocate(400, 600);
                coverOpp[2].relocate(500, 600);
            } else {
                whosTurn.relocate(150, 600);

                coverOpp[0].relocate(300, 20);
                coverOpp[1].relocate(400, 20);
                coverOpp[2].relocate(500, 20);
            }
        } else {
            checkCoverCondition1[0] = true;
            checkCoverCondition1[1] = true;
            checkCoverCondition1[2] = true;
            if (player1isBot) {

                coverOpp[0].relocate(300, 20);
                coverOpp[1].relocate(400, 20);
                coverOpp[2].relocate(500, 20);
            } else {

                coverOpp[0].relocate(300, 600);
                coverOpp[1].relocate(400, 600);
                coverOpp[2].relocate(500, 600);
            }
        }

    }

    public void moverCover(boolean player, int index) {
        if (!deck.isEmpty()) {
            if(!player1isBot&&!player2isBot)
                reSetCover();
            checkCoverNum = 6;
        }

        if (!player1isBot && !player2isBot) {
            if (!player) {
                if (index == 0) {
                    if (checkCoverCondition1[1] && checkCoverCondition1[2]) {
                        coverOpp[0].relocate(400, 600);
                        coverOpp[1].relocate(400, 600);
                        coverOpp[2].relocate(500, 600);
                    } else {
                        if (checkCoverCondition1[1]) {
                            coverOpp[0].relocate(400, 600);
                            coverOpp[1].relocate(400, 600);
                            coverOpp[2].relocate(400, 600);
                        } else {
                            coverOpp[0].relocate(500, 600);
                            coverOpp[1].relocate(500, 600);
                            coverOpp[2].relocate(500, 600);
                        }

                    }
                } else if (index == 1) {
                    if (checkCoverCondition1[0] && checkCoverCondition1[2]) {
                        coverOpp[0].relocate(300, 600);
                        coverOpp[1].relocate(300, 600);
                        coverOpp[2].relocate(500, 600);
                    } else {
                        if (checkCoverCondition1[0]) {
                            coverOpp[0].relocate(300, 600);
                            coverOpp[1].relocate(300, 600);
                            coverOpp[2].relocate(300, 600);
                        } else {
                            coverOpp[0].relocate(500, 600);
                            coverOpp[1].relocate(500, 600);
                            coverOpp[2].relocate(500, 600);
                        }

                    }

                } else {
                    if (checkCoverCondition1[0] && checkCoverCondition1[1]) {
                        coverOpp[0].relocate(300, 600);
                        coverOpp[1].relocate(400, 600);
                        coverOpp[2].relocate(400, 600);
                    } else {
                        if (checkCoverCondition1[0]) {
                            coverOpp[0].relocate(300, 600);
                            coverOpp[1].relocate(300, 600);
                            coverOpp[2].relocate(300, 600);
                        } else {
                            coverOpp[0].relocate(400, 600);
                            coverOpp[1].relocate(400, 600);
                            coverOpp[2].relocate(400, 600);
                        }

                    }

                }

            } else {
                if (index == 0) {
                    if (checkCoverCondition2[1] && checkCoverCondition2[2]) {
                        coverOpp[0].relocate(400, 20);
                        coverOpp[1].relocate(400, 20);
                        coverOpp[2].relocate(500, 20);
                    } else {
                        if (checkCoverCondition2[1]) {
                            coverOpp[0].relocate(400, 20);
                            coverOpp[1].relocate(400, 20);
                            coverOpp[2].relocate(400, 20);
                        } else {
                            coverOpp[0].relocate(500, 20);
                            coverOpp[1].relocate(500, 20);
                            coverOpp[2].relocate(500, 20);
                        }

                    }

                } else if (index == 1) {
                    if (checkCoverCondition2[0] && checkCoverCondition2[2]) {
                        coverOpp[0].relocate(300, 20);
                        coverOpp[1].relocate(300, 20);
                        coverOpp[2].relocate(500, 20);
                    } else {
                        if (checkCoverCondition2[0]) {
                            coverOpp[0].relocate(300, 20);
                            coverOpp[1].relocate(300, 20);
                            coverOpp[2].relocate(300, 20);
                        } else {
                            coverOpp[0].relocate(500, 20);
                            coverOpp[1].relocate(500, 20);
                            coverOpp[2].relocate(500, 20);
                        }

                    }

                } else {
                    if (checkCoverCondition2[0] && checkCoverCondition2[1]) {
                        coverOpp[0].relocate(300, 20);
                        coverOpp[1].relocate(400, 20);
                        coverOpp[2].relocate(400, 20);
                    } else {
                        if (checkCoverCondition2[0]) {
                            coverOpp[0].relocate(300, 20);
                            coverOpp[1].relocate(300, 20);
                            coverOpp[2].relocate(300, 20);
                        } else {
                            coverOpp[0].relocate(400, 20);
                            coverOpp[1].relocate(400, 20);
                            coverOpp[2].relocate(400, 20);
                        }

                    }

                }

            }

            if (checkCoverNum == -1) {
                root.getChildren().removeAll(coverOpp[0], coverOpp[1], coverOpp[2]);
            }

        }
        if (deck.isEmpty()) {
            if (!player) {
                checkCoverCondition1[index] = false;
            } else {
                checkCoverCondition2[index] = false;
            }
            checkCoverNum--;
        }
    }
}