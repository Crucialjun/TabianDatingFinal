package codingwithmitch.com.tabiandating;

import codingwithmitch.com.tabiandating.models.Message;
import codingwithmitch.com.tabiandating.models.User;

public interface IMainActivity   {
    void inflateViewProfileFragment(User user);
    void onMessageSelected(Message message);
    void onBackPressed();

}
