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
    /// Interaction logic for EditUser.xaml
    /// </summary>
    public partial class EditUser : Window
    {
        public string result = "false";
        public string EditUserID;
        public EditUser()
        {
            InitializeComponent();
            EditUserID = null;
        }

        public EditUser(Object NewUser)
        {
            InitializeComponent();
            if(NewUser is User)
            {

                EditUserID = ((User)NewUser).UserID;
                UserID.Text = ((User)NewUser).UserID;
                UserID.IsEnabled = false;
                UserName.Text = ((User)NewUser).Name;
                UserEmail.Text = ((User)NewUser).Email;
            }
        }

        private void AddUser(object sender, RoutedEventArgs e)
        {
            if (string.IsNullOrEmpty(UserID.Text) || string.IsNullOrEmpty(UserName.Text) || string.IsNullOrEmpty(UserEmail.Text) || string.IsNullOrEmpty(UserPassword.Password))
                MessageBox.Show("Error! One or more fields are missing!");
            else if (!userNameLoginCheck(UserEmail.Text))
                MessageBox.Show("Error! Wrong email!");
            else if (checkForSQLInjection(UserPassword.Password) || checkForSQLInjection(UserName.Text) || checkForSQLInjection(UserEmail.Text))
                MessageBox.Show("Invalid password.\n SQL Injection threat.");
            else if(EditUserID != null && !UserID.Text.Equals(EditUserID))
                MessageBox.Show("You can't change user password.");
            else
            {
                if(EditUserID == null) result = new Server().AddUser(UserID.Text, UserName.Text, UserEmail.Text, SHA.sha(UserPassword.Password));
                else result = new Server().EditUser(UserID.Text, UserName.Text, UserEmail.Text, SHA.sha(UserPassword.Password));
                this.Close();
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
                if ((CheckString.IndexOf(sqlCheckList[i], StringComparison.OrdinalIgnoreCase) >= 0))
                {
                    isSQLInjection = true;
                }
            }
            return isSQLInjection;
        }

        private void NamePreviewTextInput(object sender, TextCompositionEventArgs e)
        {
            Regex regex = new Regex("[^a-zA-Z0-9 ]+");
            e.Handled = regex.IsMatch(e.Text);
        }

        private void IDPreviewTextInput(object sender, TextCompositionEventArgs e)
        {
            Regex regex = new Regex("[^0-9]+");
            e.Handled = regex.IsMatch(e.Text);
        }

        private void EmailPreviewTextInput(object sender, TextCompositionEventArgs e)
        {
            Regex regex = new Regex("[^a-zA-Z@0-9.]+");
            e.Handled = regex.IsMatch(e.Text);
        }

        private void Close(object sender, RoutedEventArgs e)
        {
            result = "true";
            this.Close();
        }
    }
}
