package com.google.developer.udacityalumni;

import com.google.developer.udacityalumni.utility.Utility;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Divya on 1/13/2017.
 */

public class UtilityUnitTest {

    @Test
    public void getTimeInMillisTest() {
        assertEquals("1481492911000", String.valueOf(Utility.getTimeInMillis("2016-12-12T03:18:31.275Z")));
    }

    @Test
    public void fetchUrlTest() throws IOException {
        assertNotNull(Utility.fetch("http://udacity-alumni-api.herokuapp.com/api/v1/articles"));
    }
}
