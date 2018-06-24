using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace VotingSystem
{
    /// <summary>
    /// Interaction logic for AssignedUsers.xaml
    /// </summary>
    public partial class AssignedUsers : Window
    {
        List<User> AllUsers = new List<User>();
        List<User> AllAssignedUsers = new List<User>();
        public string result = "false";
        string VoteNum;
        public AssignedUsers(string VoteNum)
        {
            InitializeComponent();
            this.VoteNum = VoteNum;
        }
        private void UsersLoading(object sender, RoutedEventArgs e)
        {
            try
            {
                UserSingleton.GetInstance();
                AllUsers = new Server().SetUsers();
                if (AllUsers != null)
                {
                    UsersList.ItemsSource = AllUsers;
                    UsersList.SelectedIndex = 0;
                }
            }
            catch (Exception msg)
            {
                LoginWindow window = new LoginWindow();
                window.Owner = Window.GetWindow(this);
                window.ShowDialog();
                UsersLoading(sender, e);
            }
            //Error message
        }

        private void AssignedUsersLoading(object sender, RoutedEventArgs e)
        {
            try
            {
                UserSingleton.GetInstance();
                AllAssignedUsers = new Server().SetAssignedUsers(VoteNum);
                if (AllAssignedUsers != null)
                {
                    AssignedUsersList.ItemsSource = AllAssignedUsers;
                    AssignedUsersList.SelectedIndex = 0;

                    // AllUsers = AllUsers.Except(AllAssignedUsers).ToList();

                    //foreach (User u in AllAssignedUsers) AllUsers.Remove(u);

                    AllUsers.RemoveAll(a => AllAssignedUsers.Exists(b => a.UserID == b.UserID));

                    UsersList.ItemsSource = null;
                    UsersList.ItemsSource = AllUsers;
                    UsersList.SelectedIndex = 0;
                }
            }
            catch (Exception msg)
            {
                LoginWindow window = new LoginWindow();
                window.Owner = Window.GetWindow(this);
                window.ShowDialog();
                UsersLoading(sender, e);
            }
            //Error message
        }

        private void AddAssignedUser(object sender, RoutedEventArgs e)
        {
            if (UsersList.SelectedIndex != -1)
            {
                User AssignUser = AllUsers[UsersList.SelectedIndex];
                AllUsers.RemoveAt(UsersList.SelectedIndex);
                AllAssignedUsers.Add(AssignUser);

                UsersList.ItemsSource = null;
                UsersList.ItemsSource = AllUsers;
                UsersList.SelectedIndex = 0;

                AssignedUsersList.ItemsSource = null;
                AssignedUsersList.ItemsSource = AllAssignedUsers;
                AssignedUsersList.SelectedIndex = 0;
            }
        }

        private void RemoveAssignedUser(object sender, RoutedEventArgs e)
        {
            if (AssignedUsersList.SelectedIndex != -1)
            {
                User AssignUser = AllAssignedUsers[AssignedUsersList.SelectedIndex];
                AllAssignedUsers.RemoveAt(AssignedUsersList.SelectedIndex);
                AllUsers.Add(AssignUser);

                UsersList.ItemsSource = null;
                UsersList.ItemsSource = AllUsers;
                UsersList.SelectedIndex = 0;

                AssignedUsersList.ItemsSource = null;
                AssignedUsersList.ItemsSource = AllAssignedUsers;
                AssignedUsersList.SelectedIndex = 0;
            }
        }

        private void Close(object sender, RoutedEventArgs e)
        {
            result = "true";
            this.Close();
        }

        private void UpdateCandidates(object sender, RoutedEventArgs e)//UpdateUsers
        {
            result = new Server().UpdateUsers(VoteNum, AllAssignedUsers);
            this.Close();
        }
    }
}
