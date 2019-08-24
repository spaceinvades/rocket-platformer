package com.spaceinvds.handlers;

import com.badlogic.gdx.Screen;
import com.spaceinvds.game.Game;
import com.spaceinvds.screens.Menu;
import com.spaceinvds.screens.Pause;
import com.spaceinvds.screens.Play;
import com.spaceinvds.screens.Screens;

public class ScreenManager {
    private final Game game;
    private Play play;
    private Menu menu;
    private Pause pause;

    public ScreenManager(Game game) {
        this.game = game;
        this.menu = new Menu(this);
        this.play = new Play(this);
        this.pause = new Pause(this);
        game.setScreen(menu);
    }

    public void changeScreen(Screens s) {
        switch (s) {
            case MENU:
                if (game.getScreen().equals(play)) {
                    game.setScreen(menu);
                    disposeScreen(Screens.PLAY);
                    disposeScreen(Screens.PAUSE);
                    System.out.println("changing from play to menu");
                } else if (game.getScreen().equals(pause)) {
                    game.setScreen(menu);
                    disposeScreen(Screens.PAUSE);
                    disposeScreen(Screens.PLAY);
                    System.out.println("changing from pause to menu");
                }
                break;

            case PLAY:
                if (game.getScreen().equals(menu)) {
                    play.init();
                    game.setScreen(play);
                    disposeScreen(Screens.MENU);
                    disposeScreen(Screens.PAUSE);
                    System.out.println("changing from menu to play");
                } else if (game.getScreen().equals(pause)) {
                    game.setScreen(play);
                    disposeScreen(Screens.PAUSE);
                    System.out.println("changing from pause to play");
                }
                break;

            case PAUSE:
                if (game.getScreen().equals(play)) {
                    game.setScreen(pause);
                    System.out.println("changing from play to pause");
                } else if (game.getScreen().equals(menu))
                    break;
                else {
                    break;
                }
        }
        System.gc();
    }

    public Screen getScreen(Screens s) {
        switch (s) {
            case MENU:
                return menu;
            case PLAY:
                return play;
            case PAUSE:
                return pause;
        }
        System.out.println("Not a screen!");
        return null;
    }

    public void disposeScreen(Screens s) {
        switch (s) {
            case MENU:
                if (menu != null) {
                    menu.dispose();
                }
                break;
            case PLAY:
                if (play != null) {
                    play.dispose();
                }
                break;
            case PAUSE:
                if (pause != null) {
                    pause.dispose();
                }
                break;
        }
    }

    public Game getGame() {
        return game;
    }
}