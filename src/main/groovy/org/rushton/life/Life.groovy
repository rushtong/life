package org.rushton.life

import groovy.transform.TailRecursive
import groovy.util.logging.Slf4j

@Slf4j
class Life {

    static void main(String[] args) {

        // Seed matrix
        Matrix matrix = new Matrix(5, 6)
                .setValue(0, 0, true)
                .setValue(1, 1, true)
                .setValue(1, 4, true)
                .setValue(2, 2, true)
                .setValue(3, 1, true)
                .setValue(3, 4, true)
                .setValue(4, 1, true)
                .setValue(4, 4, true)
        // Block
//        Matrix matrix = new Matrix(4, 4)
//                .setValue(1, 1, true)
//                .setValue(1, 2, true)
//                .setValue(2, 1, true)
//                .setValue(2, 2, true)

        // Blinker (period 2)
//        Matrix matrix = new Matrix(5, 5)
//                .setValue(2, 1, true)
//                .setValue(2, 2, true)
//                .setValue(2, 3, true)

        generateChildren(Collections.singletonList(matrix), 9).each {
            log.info(it.toString())
        }

    }

    @TailRecursive
    static List<Matrix> generateChildren(List<Matrix> matrices, Integer numChildren) {
        if (matrices.size() == numChildren) {
            matrices
        } else {
            // The seed for the next child is the last element in the list
            def parent = matrices.reverse().head()
            def child = generateChild(parent)
            def newMatrices = matrices + child
            generateChildren(newMatrices, numChildren)
        }
    }

    static Matrix generateChild(Matrix parent) {
        def rows = parent.rowList.length
        def cols = parent.rowList[0].length
        Matrix child = new Matrix(rows, cols)
        (0..rows-1).each { row ->
            (0..cols-1).each { col ->
                def parentValue = parent.getCellValue(row, col)
                def neighborCount = parent.getNeighborCount(row, col)
                def childValue = false
                if (parentValue) {
                    // 1. Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
                    // NO-OP
                    // 2. Any live cell with two or three live neighbours lives on to the next generation.
                    if ([2, 3].contains(neighborCount)) childValue = true
                    // 3. Any live cell with more than three live neighbours dies, as if by overpopulation.
                    // NO-OP
                } else {
                    // 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
                    childValue = (neighborCount == 3)
                }
                child = child.setValue(row, col, childValue)
            }
        }
        child
    }
}

@Slf4j
class Matrix {

    Boolean[][] rowList

    // Neighbors are any of the 8 adjacent cells, diagonal, vertical and horizontal.
    // Iterate over all row/col combinations, except the current cell
    // and collect the values. The count of true values is the neighbor count
    // Ignore out of bounds exceptions
    @SuppressWarnings("GroovyMissingReturnStatement")
    Integer getNeighborCount(int row, int col) {
        List<Boolean> neighborValues = (row-1..row+1).collect { cellRow ->
            (col-1..col+1).collect { cellColumn ->
                def cellValue = getCellValue(cellRow, cellColumn)
                // We don't count the current cell as a neighbor
                if (cellRow == row && cellColumn == col) {
                    cellValue = false
                }
                cellValue
            }
        }.flatten().collect { it.asBoolean() }
        neighborValues.findAll{ it }.size()
    }

    Boolean getCellValue(int rowNum, int colNum) {
        try {
            this.rowList[rowNum][colNum]
        } catch (ArrayIndexOutOfBoundsException ignored) {
            // Non-existent cells are always false.
            false
        }
    }

    /**
     * General constructor for an empty matrix
     * @param rowNum
     * @param colNum
     */
    Matrix(int rowNum, int colNum) {
        def columns = (1..colNum).collect { false }
        this.rowList = (1..rowNum).collect { columns }
    }

    /**
     * Builder pattern to generate a new matrix from existing one, with updated value.
     * @param row
     * @param column
     * @param val
     * @return Populated Matrix
     */
    Matrix setValue(int row, int column, boolean val) {
        if (row < 0 || row >= this.rowList.length) {
            log.error("Row [${row}] is out of bounds (min 0, max ${this.rowList.length - 1})")
            return this
        } else if (column < 0 || column >= this.rowList[row].length) {
            log.error("Column [${column}] is out of bounds (min 0, max ${this.rowList[column].length - 1})")
            return this
        }
        def newRowList = this.rowList.clone()
        newRowList[row][column] = val
        new Matrix(newRowList)
    }

    private Matrix(Boolean[][] rowList) {
        this.rowList = rowList
    }

    @Override
    String toString() {
        List<String> rowStrings = rowList.collect { row ->
            row.collect { it ? "*" : "-" }.join(" ")
        }
        // just a printable line
        def boundary = "=".multiply(rowStrings.head().length())
        "\n" + [boundary, rowStrings.join("\n"), boundary].join("\n")
    }

}

