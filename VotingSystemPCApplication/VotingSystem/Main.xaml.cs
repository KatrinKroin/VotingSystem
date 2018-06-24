using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using System.Windows.Threading;
using Xceed.Wpf.Toolkit;

namespace VotingSystem
{
    /// <summary>
    /// Interaction logic for Main.xaml
    /// </summary>
    public partial class Main : Window
    {
        List<Voting> AllVotings;
        List<Voting> AllUserVotings;
        List<Voting> FilteredVotings;
        List<Result> AllResults;
        List<Candidate> Candidates;
        List<User> AllUsers;
        List<User> FilteredUsers;
        string StrTimer;
        DateTime Timer;
        ObservableCollection<WPFPieChart.AssetClass> classes;//Classes
        ObservableCollection<Result> tempresults;


        public Main()
        {
            InitializeComponent();
            

            /*
            List<Result> temp = new Server().SetResults("21");
            if (temp != null)
            {
                tempresults = new ObservableCollection<Result>(temp);
                this.DataContext = tempresults;
            } Loaded="VotingsLoading" Loaded="UsersLoading" 
           */
        }

        private void MainTabChanged(object sender, SelectionChangedEventArgs e)
        {
            int tabItem = ((sender as TabControl)).SelectedIndex;
            if (e.Source is TabControl) // This is a soultion of those problem.
            {
                switch (tabItem)
                {
                     
                    case 0:    // Chatting
                        VotingsList.SelectedIndex = 0;
                        break;
                    case 1:    // Users
                        UsersList.SelectedIndex = 0;
                        break;
                    default:
                        break;
                }
            }
        }

        private void VotingsLoading(object sender, RoutedEventArgs e)
        {
            try
            {
                UserSingleton UserAdmin = UserSingleton.GetInstance();
                string UserAdminID = UserAdmin.GetUserID();
                AllVotings = new Server().SetPool(UserAdminID);
                if (AllVotings != null)
                {
                    FilteredVotings = AllVotings;
                    VotingsList.ItemsSource = AllVotings;
                }
                //VotingsList.SelectedIndex = 0;
            }
            catch (Exception msg)
            {
                LoginWindow window = new LoginWindow();
                window.Owner = Window.GetWindow(this);
                window.ShowDialog();
                VotingsLoading(sender, e);
            } 
            //Error message
        }


        //UserVotingsList
        private void UserVotingsLoading(string UserID)//()
        {
            try
            {
                UserSingleton UserAdmin = UserSingleton.GetInstance();
                string UserAdminID = UserAdmin.GetUserID();
                AllUserVotings = new Server().SetUserPool(UserID);
                if (AllVotings != null) UserVotingsList.ItemsSource = AllUserVotings;
                //VotingsList.SelectedIndex = 0;
            }
            catch (Exception msg)
            {
                LoginWindow window = new LoginWindow();
                window.Owner = Window.GetWindow(this);
                window.ShowDialog();
                UserVotingsLoading(UserID);
            }
            //Error message
        }

        private void UsersLoading(object sender, RoutedEventArgs e)
        {
            try
            {
                UserSingleton.GetInstance();
                AllUsers = new Server().SetUsers();
                if (AllUsers != null) 
                {
                    FilteredUsers = AllUsers;
                    UsersList.ItemsSource = AllUsers;
                }
                //UsersList.SelectedIndex = 0; 
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

        private void dispatcherTick(object sender, EventArgs e)
        {
            TimeSpan difference = Timer.Subtract(DateTime.Now);
            if(Timer == new DateTime(0)) TimerLabel.Content = "Closed";
            else TimerLabel.Content = "Days: " + difference.Days + ",  " + difference.Hours.ToString("D2") + ":" + difference.Minutes.ToString("D2") + ":" + difference.Seconds.ToString("D2");
        }

        private void VotingsFilter_TextChanged(object sender, System.Windows.Controls.TextChangedEventArgs e)
        {
            List<Voting> NewList = new List<Voting>();
            if (!string.IsNullOrEmpty(TextFilter.Text))
            {
                foreach (Voting vote in AllVotings)
                {
                    if (vote.VoteName.Contains(TextFilter.Text))
                    {
                        NewList.Add(vote);
                    }
                }
                FilteredVotings = NewList;
                VotingsList.ItemsSource = null;
                VotingsList.ItemsSource = NewList;
                VotingsList.SelectedIndex = 0;
            }
            else
            {
                FilteredVotings = AllVotings;
                VotingsList.ItemsSource = null;
                VotingsList.ItemsSource = AllVotings;
                VotingsList.SelectedIndex = 0;
            }
        }

        private void UsersFilter_TextChanged(object sender, System.Windows.Controls.TextChangedEventArgs e)
        {
            List<User> NewList = new List<User>();
            if (!string.IsNullOrEmpty(UsersFilter.Text))
            {
                foreach (User user in AllUsers)
                {
                    if (user.Name.Contains(UsersFilter.Text))
                    {
                        NewList.Add(user);
                    }
                }
                FilteredUsers = NewList;
                UsersList.ItemsSource = null;
                UsersList.ItemsSource = NewList;
                UsersList.SelectedIndex = 0;
            }
            else
            {
                FilteredUsers = AllUsers;
                UsersList.ItemsSource = null;
                UsersList.ItemsSource = AllUsers;
                UsersList.SelectedIndex = 0;
            }
        }


        private void VotingsListBox_Click(object sender, SelectionChangedEventArgs e)
        {   
            if (VotingsList.SelectedIndex != -1)
            {
                GroupBox1.Visibility = Visibility.Visible;
                GroupBox2.Visibility = Visibility.Hidden;

                VotingInformation.IsSelected = true;

                Voting Vote = FilteredVotings[VotingsList.SelectedIndex]; 
                VotingTitle.Text = Vote.VoteName;
                VotingDescript.Text = Vote.VoteDescription;
                VotingStartDate.Text = Vote.Start.ToString("dd/MM/yyyy"); //yyyy-MM-dd
                VotingFinishDate.Text = Vote.Finish.ToString("dd/MM/yyyy");
                VotingStartTime.Text = "  " + Vote.Start.Hour.ToString("D2") + "        " + Vote.Start.Minute.ToString("D2");//  08        05
                VotingFinishTime.Text = "  " + Vote.Finish.Hour.ToString("D2") + "        " + Vote.Finish.Minute.ToString("D2");
                if (Vote.AvailableVoting()) DeleteVoting.Visibility = Visibility.Hidden;
                else DeleteVoting.Visibility = Visibility.Visible;
                if (Vote.FutureVoting())
                {    
                    EditVote.Visibility = Visibility.Visible;
                    EditAssignedUsers.Visibility = Visibility.Visible;
                    EditCustomers.Visibility = Visibility.Visible;
                }
                else
                {
                    EditVote.Visibility = Visibility.Hidden;
                    EditAssignedUsers.Visibility = Visibility.Hidden;
                    EditCustomers.Visibility = Visibility.Hidden;
                }
                //showColumnChart(Vote.VoteNum);
                // showCandidates(Vote.VoteNum);
                if (AvailableVoting(Vote.Start, Vote.Finish))
                {
                    VotingResults.IsEnabled = false;
                    Timer = Vote.Finish;
                    TimerLabel.Foreground = new SolidColorBrush(Colors.Green);
                }
                else if (!PastVoting(Vote.Finish))
                {
                    VotingResults.IsEnabled = false;
                    Timer = Vote.Start;
                    TimerLabel.Foreground = new SolidColorBrush(Colors.LightBlue);                  
                }
                else
                {
                    VotingResults.IsEnabled = true;
                    Timer = new DateTime(0);
                    TimerLabel.Foreground = new SolidColorBrush(Colors.Red);
                }
                DispatcherTimer dispatcher = new DispatcherTimer();
                dispatcher.Tick += new EventHandler(dispatcherTick);
                dispatcher.Interval = new TimeSpan(0, 0, 1);
                dispatcher.Start();
                showResults(Vote.VoteNum);
            }
            //else System.Windows.MessageBox.Show(Convert.ToString("Bad"));
        }

        private void UsersListBox_Click(object sender, SelectionChangedEventArgs e)
        {
            if (UsersList.SelectedIndex != -1)
            {
                GroupBox1.Visibility = Visibility.Hidden;
                GroupBox2.Visibility = Visibility.Visible;

                User CurrentUser = FilteredUsers[UsersList.SelectedIndex]; 
                UserID.Text = CurrentUser.UserID;
                UserName.Text = CurrentUser.Name;
                UserEmail.Text = CurrentUser.Email;
                UserPassword.Text = CurrentUser.Password;
                UserPermission.Text = CurrentUser.Admin.ToString();
                UserVotingsLoading(CurrentUser.UserID);
            }
        }

        private bool FutureVoting(DateTime Start)
        {
            DateTime Current = DateTime.Now;
            if (DateTime.Compare(Start, Current) > 0) return true;
            else return false;
        }

        private bool AvailableVoting(DateTime Start, DateTime Finish)
        {
           
            DateTime Current = DateTime.Now;
            int result1 = DateTime.Compare(Start, Current);
            int result2 = DateTime.Compare(Finish, Current);
            if (result1 < 0 && result2 > 0) return true;
            else return false;
        }

        private bool PastVoting(DateTime Finish)
        {
            DateTime Current = DateTime.Now;
            if (DateTime.Compare(Current, Finish) > 0) return true;
            else return false;
        }

        private void showColumnChart(String VoteNum)
        {
            AllResults = new Server().SetResults(VoteNum);
            List<KeyValuePair<string, int>> valueList = new List<KeyValuePair<string, int>>();

            foreach (Result result in AllResults) valueList.Add(new KeyValuePair<string, int>(result.Name, result.Amount));

            pieChart.DataContext = valueList;
        }

        private void showResults(String VoteNum)
        {
            int sum = 0;
            AllResults = new Server().SetResults(VoteNum);

            if(AllVotings!= null)
            {
                Dictionary<string, int> res = new Dictionary<string,int>();
                foreach (Result result in AllResults) res.Add(result.Name, result.Amount);
                classes = new ObservableCollection<WPFPieChart.AssetClass>(WPFPieChart.AssetClass.ConstructTestData(res));
                this.DataContext = classes;
            }

            if (AllResults != null)
            {
                foreach (Result result in AllResults) sum += result.Amount;
                foreach (Result result in AllResults) {
                    result.Persentage = Convert.ToDouble((result.Amount / (float)sum) * 100);
                    result.Representation = "" + result.Persentage.ToString("n2") + "% , " + result.Amount + " votes";
                }
            }
            CandidatesListBox.ItemsSource = AllResults;
        }

        //alluserssce
        private void showCandidates(String VoteNum)//Show
        {
            Candidates = new Server().SetCandidates(VoteNum);
            if (Candidates != null)
            {
                AllResults = new Server().SetResults(VoteNum);
               // if(AllResults != null)
               // {

               // }
                /*
                                foreach (Candidate voting in Candidates)
                                {
                                    StackPanel stk = new StackPanel();
                                    stk.Height = 27;
                                    stk.Orientation = System.Windows.Controls.Orientation.Horizontal;
                                    stk.HorizontalAlignment = System.Windows.HorizontalAlignment.Left;


                                    TextBlock txtBlk = new TextBlock();
                                    txtBlk.Text = voting.CandidateName;
                                   // txtBlk.Margin = new Thickness(30, 7, 1, 7);
                                    txtBlk.FontWeight = FontWeights.Bold;
                                   // txtBlk.Width = 162;
                                    txtBlk.VerticalAlignment = System.Windows.VerticalAlignment.Center;

                                    //<ProgressBar HorizontalAlignment="Left" Margin="554,61,0,0" Name="ProgressBar1" Width="185" Height="25" VerticalAlignment="Top" Value="75" IsIndeterminate="False" Orientation="Horizontal" IsTabStop="False" />
                                    ProgressBar prBar = new ProgressBar();
                                    prBar.Maximum = 100;
                                    prBar.FontSize = 15;
                                    prBar.Value = 20;
                                    prBar.Width = 195;
                                    prBar.VerticalAlignment = System.Windows.VerticalAlignment.Center;
                                    prBar.Margin = new Thickness(30, 7, 1, 7);

                                    //BitmapImage btm;
                                    //if (AvailableVoting(voting.Start, voting.Finish) == true) btm = new BitmapImage(new Uri("v.png", UriKind.Relative));
                                    //else btm = new BitmapImage(new Uri("close.png", UriKind.Relative));
                                   // Image img = new Image();
                                    //img.Source = btm;
                               //     img.Height = 30;
                //
                               //     TextBlock txtBlk = new TextBlock();
                               //     txtBlk.Text = voting.VoteName;
                              //      txtBlk.Margin = new Thickness(30, 7, 1, 7);
                              ///      txtBlk.FontWeight = FontWeights.Bold;
                              ////      txtBlk.Width = 162;
                              //      txtBlk.VerticalAlignment = System.Windows.VerticalAlignment.Center;

                                    //stk.Children.Add(img);
                                    stk.Children.Add(txtBlk);
                                    stk.Children.Add(prBar);

                                    CandidatesListBox.Items.Add(stk);
                                    */
                // }
                CandidatesListBox.ItemsSource = Candidates;
            }

        }


        private void AddVoting(object sender, RoutedEventArgs e)
        {
            EditVoting window = new EditVoting();
            window.Owner = Window.GetWindow(this);
            window.ShowDialog();
            if (!(window.result).Equals("true")) System.Windows.MessageBox.Show("Error! " + window.result);
            else VotingsLoading(sender, e);
            VotingsList.SelectedIndex = 0;
        }

        private void AddUser(object sender, RoutedEventArgs e)
        {
            EditUser window = new EditUser();
            window.Owner = Window.GetWindow(this);
            window.ShowDialog();
            if (!(window.result).Equals("true")) System.Windows.MessageBox.Show("Error! " + window.result);
            else UsersLoading(sender, e);
            UsersList.SelectedIndex = 0;
        }

        private void EditCurrentUser(object sender, RoutedEventArgs e)
        {
            if (UsersList.SelectedIndex != -1)
            {

                User CurrentUser = AllUsers[UsersList.SelectedIndex];
                EditUser window = new EditUser(CurrentUser);
                window.Owner = Window.GetWindow(this);
                window.ShowDialog();
                if (!(window.result).Equals("true")) System.Windows.MessageBox.Show("Error! " + window.result);
                else UsersLoading(sender, e);
                UsersList.SelectedIndex = 0;
            }
        }

        private void RemoveVoting(object sender, RoutedEventArgs e)
        {
            if (VotingsList.SelectedIndex != -1)
            {
                MessageBoxResult messageBoxResult = System.Windows.MessageBox.Show("Are you sure you want to delete this election?",null, System.Windows.MessageBoxButton.YesNo);
                if (messageBoxResult == MessageBoxResult.Yes)
                {
                    Voting Vote = AllVotings[VotingsList.SelectedIndex];
                    string answer = new Server().RemoveVoting(Vote.VoteNum);
                    if (!answer.Equals("true")) System.Windows.MessageBox.Show("Error! " + answer);
                    else VotingsLoading(sender, e);
                    VotingsList.SelectedIndex = 0;
                }                
            }
        }

        private void EditVoting(object sender, RoutedEventArgs e)
        {
            if (VotingsList.SelectedIndex != -1)
            {
                Voting Vote = AllVotings[VotingsList.SelectedIndex];
                EditVoting window = new EditVoting(Vote);
                window.Owner = Window.GetWindow(this);
                window.ShowDialog();
                if (!(window.result).Equals("true")) System.Windows.MessageBox.Show("Error! " + window.result);
                else VotingsLoading(sender, e);
                VotingsList.SelectedIndex = 0;
            }
        }

        private void RemoveCurrentUser(object sender, RoutedEventArgs e)
        {
            if (UsersList.SelectedIndex != -1)
            {
                MessageBoxResult messageBoxResult = System.Windows.MessageBox.Show("Are you sure you want to delete this user?", null, System.Windows.MessageBoxButton.YesNo);
                if (messageBoxResult == MessageBoxResult.Yes)
                {
                    User CurrentUser = AllUsers[UsersList.SelectedIndex];
                    string answer = new Server().RemoveUser(CurrentUser.UserID);
                    if (!answer.Equals("true")) System.Windows.MessageBox.Show("Error! " + answer);
                    else UsersLoading(sender, e);
                    UsersList.SelectedIndex = 0;
                }
            }
        }

        private void AssignUsers(object sender, RoutedEventArgs e)
        {
            if (VotingsList.SelectedIndex != -1)
            {
                Voting Vote = AllVotings[VotingsList.SelectedIndex];
                AssignedUsers window = new AssignedUsers(Vote.VoteNum);
                window.Owner = Window.GetWindow(this);
                window.ShowDialog();
                if (!(window.result).Equals("true")) System.Windows.MessageBox.Show("Error! " + window.result);
            }
        }

        private void AssignCandidates(object sender, RoutedEventArgs e)
        {
            if (VotingsList.SelectedIndex != -1)
            {
                Voting Vote = AllVotings[VotingsList.SelectedIndex];
                AssignedCandidates window = new AssignedCandidates(Vote.VoteNum);
                window.Owner = Window.GetWindow(this);
                window.ShowDialog();
                if (!(window.result).Equals("true")) System.Windows.MessageBox.Show("Error! " + window.result);
                else showResults(Vote.VoteNum);
            }
        }

        private void Close(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
