/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import commons.Board;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainCtrlTest {

    private MainCtrl sut;
    //@Mock
   // private Board mockBoard;
    @Mock
    private BoardCtrl mockBoardCtrl;
    @Mock
    private Stage mockPrimaryStage;

    @BeforeEach
    public void setup() {
        sut = new MainCtrl();
    }

    @Test
    void testUpdateBoard() {

        Board mockBoard = new Board();


        mockBoard.setId(1L);
        mockBoard.setTitle("Board");

        Board board = new Board();
        mockBoardCtrl.updateBoard(mockBoard);
        assertEquals(mockBoard, board);

        verify(mockBoardCtrl).updateBoard(mockBoard);
    }

    @Test
    void testShowOverview(){
        sut.showOverview();

        verify(mockPrimaryStage).setTitle("Quotes: Overview");
        verify(mockPrimaryStage).setScene(sut.getOverview());
    }


    @Test
    public void writeSomeTests() {
        // TODO create replacement objects and write some tests
        // sut.initialize(null, null, null);
    }
}