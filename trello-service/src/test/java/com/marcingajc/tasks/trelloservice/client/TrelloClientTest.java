package com.marcingajc.tasks.trelloservice.client;

import com.marcingajc.tasks.trelloservice.domain.*;
import com.marcingajc.tasks.trelloservice.config.TrelloConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrelloClientTest {

    @InjectMocks
    private TrelloClient trelloClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TrelloConfig trelloConfig;


    @Before
    public void init() {

        when(trelloConfig.getTrelloApiEndpoint()).thenReturn("http://test.com");
        when(trelloConfig.getTrelloAppKey()).thenReturn("test");
        when(trelloConfig.getTrelloToken()).thenReturn("test");
        when(trelloConfig.getTrelloUsername()).thenReturn("cur1osity");
    }

    @Test
    public void shouldFetchTrelloBoards() throws URISyntaxException{

        //  Given
        TrelloBoardDto[] trelloBoards = new TrelloBoardDto[1];
        trelloBoards[0] = new TrelloBoardDto("test_id", "test_board", new ArrayList<>());
        URI uri = new URI("http://test.com/members/cur1osity/boards?key=test&token=test&fields=name,id&lists=open");
        when(restTemplate.getForObject(uri, TrelloBoardDto[].class)).thenReturn(trelloBoards);

        //  When
        List<TrelloBoardDto> fetchedTrelloBoards = trelloClient.getTrelloBoards();

        //  Then
        assertEquals(1, fetchedTrelloBoards.size());
        assertEquals("test_id", fetchedTrelloBoards.get(0).getId());
        assertEquals("test_board", fetchedTrelloBoards.get(0).getName());
        assertEquals(new ArrayList<>(), fetchedTrelloBoards.get(0).getLists());
    }

    @Test
    public void shouldFetchTrelloBoard() throws URISyntaxException{

        //  Given
        List<TrelloListDto> trelloListDto = new ArrayList<>();
        TrelloBoardDto trelloBoard = new TrelloBoardDto("1", "test_board",trelloListDto);
        URI uri = new URI("http://test.com/board/1?key=test&token=test&fields=name,id&lists=open");
        when(restTemplate.getForObject(uri, TrelloBoardDto.class)).thenReturn(trelloBoard);

        //  When
        TrelloBoardDto fetchedTrelloBoard = trelloClient.getTrelloBoard("1");

        //  Then
        assertEquals("1", fetchedTrelloBoard.getId());
        assertEquals("test_board", fetchedTrelloBoard.getName());
        assertEquals(new ArrayList<>(), fetchedTrelloBoard.getLists());
    }

    @Test
    public void shouldCreateCard() throws URISyntaxException {

        // Given
        TrelloCardDto trelloCardDto = new TrelloCardDto (
                "Test task",
                "Test description",
                "top",
                "test_id"
        );


        // When
        URI uri = new URI("http://test.com/cards?key=test&token=test&name=Test%20task&desc=Test%20description&pos=top&idList=test_id");

        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto(
                "1",
                "Test task",
                "http://test.com"
                // new TrelloBadgesDto(0, new TrelloAttachmentsByTypeDto(new TrelloTrelloDto(0, 0)))
        );

        when(restTemplate.postForObject(uri, null, CreatedTrelloCardDto.class)).thenReturn(createdTrelloCardDto);

        CreatedTrelloCardDto newCard = trelloClient.createNewCard(trelloCardDto);

        // Then
        assertEquals("1", newCard.getId());
        assertEquals("Test task", newCard.getName());
        assertEquals("http://test.com", newCard.getShortUrl());
        //    assertEquals(0, newCard.getTrelloBadgesDto().getVotes());
        //    assertEquals(0, newCard.getTrelloBadgesDto().getTrelloAttachmentsByTypeDto().getTrelloTrelloDto().getBoard());
        //    assertEquals(0, newCard.getTrelloBadgesDto().getTrelloAttachmentsByTypeDto().getTrelloTrelloDto().getCard());
    }


    @Test
    public void shouldReturnEmptyList() throws URISyntaxException {

        //  Given
        TrelloBoardDto[] trelloBoards = new TrelloBoardDto[0];
        URI uri = new URI("http://test.com/members/cur1osity/boards?key=test&token=test&fields=name,id&lists=open");
        when(restTemplate.getForObject(uri, TrelloBoardDto[].class)).thenReturn(trelloBoards);

        //  When
        List<TrelloBoardDto> fetchedTrelloBoards = trelloClient.getTrelloBoards();

        //  Then
        assertEquals(0, fetchedTrelloBoards.size());
    }
}