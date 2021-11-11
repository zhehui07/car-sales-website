package group6;


import group8.Car;

/**
 * @author Runyao Xia
 * @date: 2021/4/10
 */
public class FormAction {
    private User user;
    private Integer id;
    private FormActionDirectory fd;
    public FormAction(User user) {
        this.user = user;
        user.setForm(this);
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "FormAction{" +
                "user=" + user +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public FormActionDirectory getFd() {
        return fd;
    }

    public void setFd(FormActionDirectory fd) {
        this.fd = fd;
    }
}
