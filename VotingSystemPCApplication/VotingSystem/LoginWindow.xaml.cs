using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Mail;
using System.Text;
using System.Text.RegularExpressions;
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
    /// Interaction logic for LoginWindow.xaml
    /// </summary>
    public partial class LoginWindow : Window
    {
        public LoginWindow()
        {
            InitializeComponent();
        }

        private void Exit(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        private void TextBox_GotKeyboardFocus(object sender, KeyboardFocusChangedEventArgs e)
        {
            TextBox txtBox = sender as TextBox;
            if (txtBox.Text.Equals("Concentration"))
                txtBox.Text = string.Empty;
        }

        private void passwordTBOnKeyDownHandler(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Return)
            {
                EnterLogInButton.RaiseEvent(new RoutedEventArgs(Button.ClickEvent));
            }
        }

        private void texbox_PreviewTextInput(object sender, TextCompositionEventArgs e)
        {
            Regex regex = new Regex("[^a-zA-Z@0-9.]+");
            e.Handled = regex.IsMatch(e.Text);
        }

        private void Login(object sender, RoutedEventArgs e)
        {
            if (string.IsNullOrWhiteSpace(textBox3.Text) || string.IsNullOrWhiteSpace(UserLoginPassword.Password))
                MessageBox.Show("Error! One or more fields are missing!");
            else if (!userNameLoginCheck(textBox3.Text))
                MessageBox.Show("Error! Wrong email!");
            else if (checkForSQLInjection(UserLoginPassword.Password) || checkForSQLInjection(textBox3.Text))
                MessageBox.Show("Invalid password.\n SQL Injection threat.");
            else
            {
                try
                {
                    UserSingleton.Create(textBox3.Text, SHA.sha(UserLoginPassword.Password));
                    UserSingleton.GetInstance();
                    this.Close();
                }
                catch (Exception msg)
                {
                    MessageBox.Show(msg.Message);
                }
                //new Server().Login(Email, SHA.sha(Password));
                //currentUser.User = user;
            }
        }

        public static bool userNameLoginCheck(string username)
        {
            if (username.StartsWith(".") || username.StartsWith(" ")) return false;
            try
            {
                MailAddress m = new MailAddress(username);
                return true;
            }
            catch (FormatException msg)
            {
                return false;
            }
        }

        public static Boolean checkForSQLInjection(string userInput)
        {
            bool isSQLInjection = false;
            string[] sqlCheckList = { "--", ";--", ";", "/*", "*/", "@@", "char", "nchar", "varchar", "nvarchar",
                                      "alter", "begin", "cast", "create", "cursor", "declare", "delete", "drop", "end", "exec",
                                      "execute", "fetch", "insert", "kill", "select", "sys", "sysobjects", "syscolumns", "table",  "update"
                                     };
            string CheckString = userInput.Replace("'", "''");
            for (int i = 0; i <= sqlCheckList.Length - 1; i++)
            {
                if ((CheckString.IndexOf(sqlCheckList[i],StringComparison.OrdinalIgnoreCase) >= 0))
                {
                    isSQLInjection = true;
                }
            }
            return isSQLInjection;
        }

        private void Close(object sender, RoutedEventArgs e)
        {
            Environment.Exit(0);
        }
    }
}
