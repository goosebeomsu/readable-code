package cleancode.minesweeper.tobe.minesweeper.board;


import cleancode.minesweeper.tobe.minesweeper.board.cell.*;
import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;

import java.util.List;
import java.util.Stack;


public class GameBoard {

    private final Cell[][] board;
    private final int landMineCount;
    private GameStatus gameStatus;


    public GameBoard(GameLevel gameLevel) {
        board = new Cell[gameLevel.getRowSize()][gameLevel.getColSize()];
        landMineCount = gameLevel.getLandMineCount();
        initGameStatus();
    }

    // 상태 변경
    public void initGame() {
        initGameStatus();
        CellPositions cellPositions = CellPositions.from(board);

        initEmptyCells(cellPositions);

        List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
        initLandMineCells(landMinePositions);

        List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
        initNumberCells(numberPositionCandidates);
    }

    public void flagAt(CellPosition cellPosition) {
        findCell(cellPosition).flag();
        checkIfGameIsOver();
    }

    public void openAt(CellPosition cellposition) {
        if (isLandMineCellAt(cellposition)) {
            openOneCellAt(cellposition);
            changeGameStatusToLose();
            return;
        }

//        openSurroundedCells(cellposition);
        openSurroundedCells2(cellposition);
        checkIfGameIsOver();
    }

    // 판별
    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        return cellPosition.isRowIndexMoreThanOrEqual(rowSize) || cellPosition.isColIndexMoreThanOrEqual(colSize);
    }

    public boolean isInProgress() {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    public boolean isWinStatus() {
        return gameStatus == GameStatus.WIN;
    }

    public boolean isLooseStatus() {
        return gameStatus == GameStatus.LOSE;
    }

    // 조회
    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    public CellSnapshot getSnapshot(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getSnapshot();
    }

    private void initGameStatus() {
        gameStatus = GameStatus.IN_PROGRESS;
    }

    private void initEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getPositions();
        for (CellPosition position : allPositions) {
            updateCellAt(position, new EmptyCell());
        }
    }

    private void initLandMineCells(List<CellPosition> landMinePositions) {
        for (CellPosition position : landMinePositions) {
            updateCellAt(position, new LandMineCell());
        }
    }

    private void initNumberCells(List<CellPosition> numberPositionCandidates) {
        for (CellPosition candidatePosition : numberPositionCandidates) {
            int count = countNearbyLandMines(candidatePosition);
            if (count != 0) {
                updateCellAt(candidatePosition, new NumberCell(count));
            }
        }
    }

    private void updateCellAt(CellPosition position, Cell cell) {
        board[position.getRowIndex()][position.getColIndex()] = cell;
    }

    private int countNearbyLandMines(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        long count = calculateSurroundedPositions(cellPosition, rowSize, colSize).stream().filter(this::isLandMineCellAt).count();

        return (int) count;
    }

    private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
        return RelativePosition.SURROUNDED_POSITIONS.stream().filter(cellPosition::canCalculatePositionBy).map(cellPosition::calculatePositionBy).filter(position -> position.isRowIndexLessThan(rowSize)).filter(position -> position.isColIndexLessThan(colSize)).toList();
    }

    private void openSurroundedCells(CellPosition cellPosition) {

        if (isOpenedCell(cellPosition)) {
            return;
        }

        if (isLandMineCellAt(cellPosition)) {
            return;
        }

        openOneCellAt(cellPosition);

        if (doesCellHaveLandMineCount(cellPosition)) {
            return;
        }

        List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize());
        surroundedPositions.forEach(this::openSurroundedCells);
    }

    private void openSurroundedCells2(CellPosition cellPosition) {
        Stack<CellPosition> stack = new Stack<>();
        stack.push(cellPosition);

        while (!stack.isEmpty()) {
            openAndPushCellAt(stack);
        }
    }

    private void openAndPushCellAt(Stack<CellPosition> stack) {
        CellPosition currentCellPosition = stack.pop();
        if (isOpenedCell(currentCellPosition)) {
            return;
        }

        if (isLandMineCellAt(currentCellPosition)) {
            return;
        }

        openOneCellAt(currentCellPosition);

        if (doesCellHaveLandMineCount(currentCellPosition)) {
            return;
        }

        List<CellPosition> surroundedPositions = calculateSurroundedPositions(currentCellPosition, getRowSize(), getColSize());
        for (CellPosition surroundedPosition : surroundedPositions) {
            stack.push(surroundedPosition);
        }

    }

    private void openOneCellAt(CellPosition cellPosition) {
        findCell(cellPosition).open();
    }

    private boolean isOpenedCell(CellPosition cellPosition) {
        return findCell(cellPosition).isOpened();
    }

    private boolean isLandMineCellAt(CellPosition cellPosition) {
        return findCell(cellPosition).isLandMine();
    }

    private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
        return findCell(cellPosition).hasLandMineCount();
    }

    private void checkIfGameIsOver() {
        if (isAllCellsChecked()) {
            changeGameStatusToWin();
        }
    }

    private void changeGameStatusToWin() {
        gameStatus = GameStatus.WIN;
    }

    private boolean isAllCellsChecked() {
        Cells cells = Cells.from(board);
        return cells.isAllChecked();
    }


    private void changeGameStatusToLose() {
        gameStatus = GameStatus.LOSE;
    }

    private Cell findCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
    }

}
