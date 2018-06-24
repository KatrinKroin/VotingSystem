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
    /// Interaction logic for Login.xaml
    /// </summary>
    public partial class Login : Window
    {
        public Login()
        {
            InitializeComponent();
        }
     
        private void passwordTBOnKeyDownHandler(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Return)
            {
                EnterLogInButton.RaiseEvent(new RoutedEventArgs(Button.ClickEvent));
            }
        }

        private void LogInButton(object sender, RoutedEventArgs e)
        {
           // if (User.userNameLoginCheck(this, UserLoginEmail.Text) == true && User.userPasswordLoginCheck(this, UserLoginPassword.Password) == true)
           // {
                try
                {
                    UserSingleton.Create(UserLoginEmail.Text, SHA.sha(UserLoginPassword.Password));
                }
                catch (Exception msg)
                {
                    MessageBox.Show(msg.ToString());
                }
                //new Server().Login(Email, SHA.sha(Password));
                //currentUser.User = user;
                try
                {
                    UserSingleton.GetInstance();
                    this.Close();
                }
                catch (Exception msg)
                {
                    MessageBox.Show(msg.Message);
                }
           // }     
        }

        private void ExitLogIn(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
