package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.config.GameConfig;
import cleancode.minesweeper.tobe.gamelevel.Beginner;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class GameApplication {

    public static void main(String[] args) {

        GameConfig gameConfig = new GameConfig(
                new Beginner(),
                new ConsoleInputHandler(),
                new ConsoleOutputHandler());

        Minesweeper minesweeper = new Minesweeper(gameConfig);
        minesweeper.initialize();
        minesweeper.run();
    }

    /**
     * DIP
     *
     * DI: 필요한 의존성을 외부에서 주입 - "3"
     * a 객체가 b 객체를 필요로할때 a가 b를 생성하는게 아니라 주입받고자 할때 제 3 자가 해야함 (ioc 컨테이너)
     *
     * IoC: 객체 생명주기를 ioc 컨테이너에서 다룸 (프로그램의 제어권이 개발자가아닌 프레임워크로)
     *
     */

}
