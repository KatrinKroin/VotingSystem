   M S B A M L   `   `   `  ����G  CVotingSystem, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null+clr-namespace:VotingSystemVotingSystem  5   	       VotingSystem.LoginWindow   5   	   W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   ,localclr-namespace:VotingSystem     5   	     � Title$  LoginWindow��5   	   $	��190��6   $��310.6��6+    � WindowStyle$
 None=�69    � AllowsTransparency .  � WindowStartupLocation$ CenterScreen=�6f   ��5
      �� 5   
    �� ImageSource$ /image/bck.png��6   5      .��5      �  � RowDefinitions 5   
   �� 5      $	��33* �6   �� 5      $	��33* �6   �� 5      $	��33* �6   �� 5      $	��33* �6   �� 5      $	��33* �6   �� 5      $	��33* �6   �� 5      $	��33* �6    � ColumnDefinitions 5   
   �� 5      $
��189* �6   .+�5   
   �� $��/image/scevoting.png��6   $��0,2,0,16q�6.   $��3��6@   �� 5   
   -   5         ��Name$ 
Emaillabel��5      $��Email��6$   $
��Bold"�64   
�������$��7,24,3,5q�6Z    J� RenderTransformOrigin$ 0.5,0.5&�6l   $��1��6�   $��2��6�   $��1��6�   �� 5   
   -   5        $ Passwordlabel��5      $��Password��6'   $
��Bold"�6:   
�������$��7,0,3,0q�6`   $��4��6q   �� 5   
   -   5        	 ��Name$	 EnterLogInButton��5      $��Login��6,   
�������$��	280,0,5,4q�6R   $��	Whimsy TT)�6s   $��6��6�   ��5       �� 5!      .R�5"      �� $��/image/Venstre_DK_V.png��6   5#      5%      �� 5&   
   -   5        
 ��Name$
 textBox3��5&       �� 	MaxLength$ 35��6$   
������ 
�������$
��Left=�6q   $��23��6�   $��10,0,0,0q�6�    �� TextWrapping$
 Wrap=�6�   $	��Top=�6�   $	��291��6�   
�������$��3��6M  ��63   Z� .��6Z   � ToolTip 5'      _� 5(      
����� �$��	Whimsy TT)�6-   .��6D   IGPollution concentration of the factory waste (maximum 100 milliliters).5*      .��5+      5,      G� 5-   
   -   5         G�Name$ UserLoginPassword��5-       G� 	MaxLength$ 20��61   
�������
�������$��	10,3,10,5q�6g   $
��Auto��6z   $��1��6�   $��5��6�   ��6�   Z� 6�   �� 5.   
   -   5        $��closeall��5.   n   $��
RemoveVote��6    J� Opacity$	 0.7Q�6&   $��26��64   $
��Left=�6@   $��	283,2,0,0q�6[   $	��Top=�6~   $��28��6�   $��2��6�   ��5/      �� 50      .R�51      �� $��/image/xclose.png��6   52      54      55      57      