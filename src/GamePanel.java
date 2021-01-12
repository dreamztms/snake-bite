import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, Runnable {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS ];
    final int y [] = new int[GAME_UNITS ];
    int bodyParts = 6;
    int dotsEaten;
    int dotsX;
    int dotsY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    Graphics gp;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH , SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener( new MyKeyAdapter());
        run();

    }

    public void startGame(){
        newDot();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        gp = g;
        draw(g);
    }

    public void draw(Graphics g){
        if(running){

            g.setColor(Color.red);
            g.fillOval(dotsX,dotsY,UNIT_SIZE,UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i==0){
                    g.setColor(Color.green);
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }else{
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }
            }

            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free",Font.BOLD,40));
            FontMetrics fontMetrics = getFontMetrics(g.getFont());
            g.drawString("Score :"+dotsEaten,(SCREEN_WIDTH - fontMetrics.stringWidth("Score :"+dotsEaten))/2,g.getFont().getSize());
        }else{
            gameOver(g);
        }
    }
    public void newDot(){
        dotsX = random.nextInt ((int)(SCREEN_WIDTH / UNIT_SIZE ))*UNIT_SIZE;
        dotsY = random.nextInt ((int)(SCREEN_HEIGHT / UNIT_SIZE ))*UNIT_SIZE;
    }

    public void move( ){
        for (int i = bodyParts; i >0 ; i--) {
            x[i] =  x[i-1];
            y[i] =  y[i-1];
        }

        switch (direction){
            case 'U' : y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D' : y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L' : x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R' : x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkDot(){
        if((x[0] == dotsX) && (y[0] == dotsY)){
            bodyParts++;
            dotsEaten++;
            newDot();

        }
    }

    public void checkCollisions(){
        for (int i = bodyParts; i > 0 ; i--) {
            if((x[0]== y[i]) && (y[0]== x[i])){
                running = false;
            }
        }

        if(x[0] < 0){
            running = false;
        }

        if(x[0] > SCREEN_WIDTH){
            running = false;
        }

        if(y[0] < 0){
            running = false;
        }

        if(x[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics fontMetrics1 = getFontMetrics(g.getFont());
        g.drawString("Score :"+dotsEaten,(SCREEN_WIDTH - fontMetrics1.stringWidth("Score :"+dotsEaten))/2,g.getFont().getSize());

        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free",Font.BOLD,50));
        FontMetrics fontMetrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - fontMetrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
                if(running){
                    move();
                    checkDot();
                    checkCollisions();
                }
                if (!running) {
                    gameOver(gp);
                    Thread.currentThread().interrupt();
                }
                repaint();
                Thread.sleep(DELAY);

        } catch (InterruptedException ex) {
          gameOver(gp);
        }

    }

    @Override
    public void run() {
        Thread thread = new Thread(() -> {
            startGame();
        });
        thread.start();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT :
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT :
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP :
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN :
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
