package ca.uwaterloo.grouptalk;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Wisdom H Jiang on 2016-11-21.
 */

@RunWith(AndroidJUnit4.class)
public class TalkTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    private Solo solo;

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), mainActivityTestRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    @Test
    public void testRegister() throws Exception {
        solo.unlockScreen();
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        //test input with single digit
        solo.clickOnView(solo.getView(R.id.buttonRegister));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected Register", RegisterActivity.class);
        solo.enterText((EditText) solo.getView(R.id.registerUserId), "hai1");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.registerPassword), "123");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.registerButton));
        solo.sleep(10000);
    }

    @Test
    public void testLogin() throws Exception {
        solo.unlockScreen();
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginUserId), "hai1");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "123");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.buttonUserLogin));
        solo.sleep(10000);
        solo.assertCurrentActivity("Expected Group", GroupActivity.class);
        solo.sleep(10000);
    }

    @Test
    public void testLoginIncorrectUserId() throws Exception {
        solo.unlockScreen();
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginUserId), "hai2");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "123");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.buttonUserLogin));
        solo.sleep(10000);
    }


    @Test
    public void testGameQuit() throws Exception {
        solo.unlockScreen();
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginUserId), "hai1");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "123");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.buttonUserLogin));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected Group", GroupActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.game));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected TalkActivity", TalkActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.joinG));
        solo.sleep(3000);
        solo.clickOnView(solo.getView(R.id.quitG));
        solo.sleep(3000);
        solo.clickOnView(solo.getView(R.id.groupMemberG));
        solo.sleep(100000);
    }

    @Test
    public void testGameIn() throws Exception {
        solo.unlockScreen();
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginUserId), "hai1");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "123");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.buttonUserLogin));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected Group", GroupActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.game));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected TalkActivity", TalkActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.joinG));
        solo.sleep(3000);
        solo.clickOnView(solo.getView(R.id.groupMemberG));
        solo.sleep(100000);
    }

    @Test
    public void testSentNotJoin() throws Exception {
        solo.unlockScreen();
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginUserId), "hai1");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "123");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.buttonUserLogin));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected Group", GroupActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.game));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected TalkActivity", TalkActivity.class);
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.textToGoG), "Hello");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.sendG));
        solo.sleep(100000);
    }

    @Test
    public void testSentJoin() throws Exception {
        solo.unlockScreen();
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginUserId), "hai1");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "123");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.buttonUserLogin));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected Group", GroupActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.game));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected TalkActivity", TalkActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.joinG));
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.textToGoG), "Hello");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.sendG));
        solo.sleep(100000);
    }

    @Test
    public void testSentJoinEmptyMessage() throws Exception {
        solo.unlockScreen();
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginUserId), "hai1");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "123");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.buttonUserLogin));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected Group", GroupActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.game));
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected TalkActivity", TalkActivity.class);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.joinG));
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.textToGoG), "Hello");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.sendG));
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.sendG));
        solo.sleep(100000);
    }
}
