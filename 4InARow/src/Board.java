import org.apache.commons.lang.ArrayUtils;

/**
 * Created by Tom on 24/09/2015.
 */
public class Board {
    private int rows;
    private int columns;
    private TokenType[][] cells;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        cells = new TokenType[rows][columns];
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < columns; ++col) {
                cells[row][col] = TokenType.Empty;
            }
        }
    }



    public void paint() {
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < columns; ++col) {
                cells[row][col].paint();
                if (col < columns - 1) System.out.print("|");
            }
            System.out.println();
            if (row < rows - 1) {
                System.out.println("-----------");
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }


    public TokenType getShiveType(int row, int col) {
        return cells[row][col];
    }

    public void setShiveType(int row, int col, TokenType tokenType) {
        cells[row][col] = tokenType;
    }

    public boolean check4Win(int col, int row) {
        return (checkVertically(row, col, 5) || checkHorizontally(row, col, 5)
                || checkDiagonally1(row, col, 5)
                || checkDiagonally2(row, col, 5));

    }

    //boolean direction:
    //if true rotate clockwise
    //if false rotate counterclockwise
    public void turnBoard(int chosenPart, boolean direction) {
        TokenType[][] chosenBoard = getChosenPartFromBoard(chosenPart);

        TokenType temp;
        // transpose matrix
        /*
         from 1 2 3
              4 5 6
              7 8 9

         to   1 4 7
              2 5 8
              3 6 9
         */


        // reverse rows or columns

        // rotate clockwise
        if (direction) {
            for (int i = 0; i < chosenBoard.length / 2 + 1; i++) {
                for (int j = i; j < chosenBoard[0].length; j++) {
                    temp = chosenBoard[i][j];
                    chosenBoard[i][j] = chosenBoard[j][i];
                    chosenBoard[j][i] = temp;
                }
            }
            for (int i = 0; i < chosenBoard.length; i++) {
                ArrayUtils.reverse(chosenBoard[i]);
            }
        } else {

            // rotate counterclockwise
            int teller = 0;
            do {
                for (int i = 0; i < chosenBoard.length / 2 + 1; i++) {
                    for (int j = i; j < chosenBoard[0].length; j++) {
                        temp = chosenBoard[i][j];
                        chosenBoard[i][j] = chosenBoard[j][i];
                        chosenBoard[j][i] = temp;
                    }
                }
                for (int i = 0; i < chosenBoard.length; i++) {
                    ArrayUtils.reverse(chosenBoard[i]);
                }
                teller++;
            } while (teller < 3);
        }


        putTransposedPartInBoard(chosenBoard, chosenPart);
    }

    private TokenType[][] getChosenPartFromBoard(int chosenPart) {
        TokenType[][] chosenBoard = new TokenType[3][3];
        switch (chosenPart) {
            case 1:
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        chosenBoard[row][col] = cells[row][col];
                    }
                }
                break;
            case 2:
                for (int row = 0; row < 3; row++) {
                    for (int col = 3; col < 6; col++) {
                        chosenBoard[row][col-3] = cells[row][col];
                    }
                }
                break;
            case 3:
                for (int row = 3; row < 6; row++) {
                    for (int col = 0; col < 3; col++) {
                        chosenBoard[row-3][col] = cells[row][col];
                    }
                }
                break;
            case 4:
                for (int row = 3; row < 6; row++) {
                    for (int col = 3; col < 6; col++) {
                        chosenBoard[row-3][col-3] = cells[row][col];
                    }
                }
                break;
        }
        return chosenBoard;
    }

    private void putTransposedPartInBoard(TokenType[][] transposedPart, int chosenPart) {
        switch (chosenPart) {
            case 1:
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        cells[row][col] = transposedPart[row][col];
                    }
                }
                break;
            case 2:
                for (int row = 0; row < 3; row++) {
                    for (int col = 3; col < 6; col++) {
                        cells[row][col] = transposedPart[row][col-3];
                    }
                }
                break;
            case 3:
                for (int row = 3; row < 6; row++) {
                    for (int col = 0; col < 3; col++) {
                        cells[row][col] = transposedPart[row-3][col];
                    }
                }
                break;
            case 4:
                for (int row = 3; row < 6; row++) {
                    for (int col = 3; col < 6; col++) {
                        cells[row][col] = transposedPart[row-3][col-3];
                    }
                }
                break;
        }
    }

    public String getPlayerOfTokenAt(int row, int col) {
        if (row < rows && col < columns) {
            TokenType cell = cells[row][col];
            return cell.getColour();
        }
        return TokenType.Empty.getColour();
    }

    //for checking nrOfTokens (win situation: nrOfTokens = 5)
    private boolean checkDiagonally1(int row, int col, int nrOfTokens) {
        for (int j = 0; j < nrOfTokens; j++) {
            int adjacentSameTokens = 0;
            for (int i = 0; i < nrOfTokens; i++) {
                if ((col + i - j) >= 0 && (col + i - j) < columns
                        && (row + i - j) >= 1 && (row + i - j) < rows
                        && getPlayerOfTokenAt(row + i - j, col + i - j).equals(getPlayerOfTokenAt(row, col)) && !getPlayerOfTokenAt(row, col).equals(TokenType.Empty.getColour())) {
                    adjacentSameTokens++;
                }
            }
            if (adjacentSameTokens >= nrOfTokens)
                return true;
        }
        return false;
    }

    private boolean checkDiagonally2(int row, int col, int nrOfTokens) {
        for (int j = 0; j < nrOfTokens; j++) {
            int adjacentSameTokens = 0;
            for (int i = 0; i < nrOfTokens; i++) {
                if ((col - i + j) >= 0 && (col - i + j) < columns
                        && (row + i - j) >= 1 && (row + i - j) < rows
                        && getPlayerOfTokenAt(row + i - j, col - i + j).equals(getPlayerOfTokenAt(row, col)) && !getPlayerOfTokenAt(row, col).equals(TokenType.Empty.getColour())) {
                    adjacentSameTokens++;
                }
            }
            if (adjacentSameTokens >= nrOfTokens)
                return true;
        }
        return false;
    }

    private boolean checkHorizontally(int row, int col, int nrOfTokens) {
        int adjacentSameTokens = 1;
        int i = 1;
        while (col - i >= 0 && getPlayerOfTokenAt(row, col - i).equals(getPlayerOfTokenAt(row, col)) && !getPlayerOfTokenAt(row, col).equals(TokenType.Empty.getColour())) {
            adjacentSameTokens++;
            i++;
        }
        i = 1;
        while (col + i < columns && getPlayerOfTokenAt(row, col + i).equals(getPlayerOfTokenAt(row, col)) && !getPlayerOfTokenAt(row, col).equals(TokenType.Empty.getColour())) {
            adjacentSameTokens++;
            i++;
        }
        return (adjacentSameTokens >= nrOfTokens);
    }

    private boolean checkVertically(int row, int col, int nrOfTokens) {
        int adjacentSameTokens = 1;
        int i = 1;
        while (row + i < rows && getPlayerOfTokenAt(row + i, col).equals(getPlayerOfTokenAt(row, col)) && !getPlayerOfTokenAt(row, col).equals(TokenType.Empty.getColour())) {
            adjacentSameTokens++;
            i++;
        }
        return (adjacentSameTokens >= nrOfTokens);
    }


}
