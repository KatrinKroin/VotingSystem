   M S B A M L   `   `   `  ���� G  CVotingSystem, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null+clr-namespace:VotingSystemVotingSystem       VotingSystem.AssignedUsers   W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     @d2http://schemas.microsoft.com/expression/blend/2008     Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     ,localclr-namespace:VotingSystem       � Title$  AssignedUsers��$��394.296��$	��550�� � WindowStyle$
 None=� � AllowsTransparency .  � WindowStartupLocation$ CenterScreen=�����  �� ImageSource$ /image/bck.png��.��� .+��� $
��Bold"�
�������$��Assigned users:��$
��Left=�$��
372,36,0,0q�$	��Top=�$��98���� $
��Bold"�
�������$��Unassigned users:��$
��Left=�$��	71,36,0,0q�$	��Top=�$	��113���� -    ��Name$ 	UsersList�� J� Opacity$
 0.75Q� �� SelectedIndex$ 0��
�������$��10,62,300,51q�����  �� EndPoint$ 0.5,1&�	 �� 
StartPoint$	 0.5,0&�.��� 
 � Color$
 Black�� � Offset$ 0Q�� $
 	#FF565268��$
 0.46Q����� .R��  � ColumnDefinitions �� $��35 ��� $	��150 � � RowDefinitions �� �� .+��� $
��Left=����� *UserPicture+�� $��Center=�$��0��$��1��
�������$
��Bold"����� *UserID+�� -   $
��Left��$��Add User��$
 0.75Q�$��37��$
��Left=�$��250,229,0,0q�$	��Top=�$��50������ .R��� $��/image/Arrow_Right.png��$��0,0,7,0q��� -   $��Right��$��Add User��$
 0.75Q�$��37��$
��Left=�$��255,147,0,0q�$	��Top=�$��45������ .R��� $��/image/Arrow_Left.png��$��0,0,7,0q��� -   $ AssignedUsersList��$
 0.75Q�$ 0��
�������$��300,62,10,51q����� $ 0.5,1&�$	 0.5,0&�.��� $
 Black��$ 0Q�� $
 	#FF565268��$
 0.46Q����� .R��  �� $��35 ��� $	��150 � �� �� .+��� $
��Left=����� *UserPicture+�� $��Center=�$��0��$��1��
�������$
��Bold"����� *UserID+�� -    ��Name$ close��$
 0.85Q�$��Button��$
��Left=�$��
519,10,0,0q�$	��Top=�$��21��$��21�� J� RenderTransformOrigin$ 0.5,0.5&����� .R��� $��/image/xclose.png���� -   $ button��$��Button��$
��Left=�$��499,348,0,0q�$	��Top=�$��41��$��36������ .R��� $��/image/Venstre_DK_V.png��