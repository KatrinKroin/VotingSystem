   M S B A M L   `   `   `  ����G  CVotingSystem, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null+clr-namespace:VotingSystemVotingSystem  5   	   %    VotingSystem.AssignedCandidates   5   	   W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   ,localclr-namespace:VotingSystem     5   	     � Title$  AssignedCandidates��5   	   $��394.296��6$   $	��450��65    � WindowStyle$
 None=�6A    � AllowsTransparency .  � WindowStartupLocation$ CenterScreen=�6n   ��5	      �� 5
   
    �� ImageSource$ /image/bck.png��6   5      .��5      � .+�5   
   �� $
��Bold"�6   
�������$��Add new candidate:��66   $
��Left=�6S   $��
141,24,0,0q�6n   $	��Top=�6�   $	��128��6�   �� 5   
   -   5         ��Name$ CandudatesList��5       J� Opacity$
 0.75Q�6*    �� SelectedIndex$ 0��69   
�������$��22,119,66,32q�6_   ��5      �� 5       �� EndPoint$ 0.5,1&�6&   	 �� 
StartPoint$	 0.5,0&�67   .��5      � 
 � Color$
 Black��6#    � Offset$ 0Q�61   � 5      $
 	#FF565268��6#   $
 0.46Q�65   5      ��5      �� 5      .R�5      �  � ColumnDefinitions 5      �� 5      $��35 �6/   �� 5      $	��250 �6/   �� 5      $��35 �6/    � RowDefinitions 5      �� 5      �� 5       .+�5"      �� $
��Left=�6D   ��6    �� *CandidatePicture+6D   �� 5#      $��Center=�6!   $��0��6`   $��1��6m   
�������$
��Bold"�6�   ��6>   �� *CandidateName+6�   �� 5$      $��EditAssignedUsers��6r   -   5        $��RemoveCandidate��5$   !   $��31��6;   $
��Left=�6G   $��2��6b   $	��Top=�6�   $��35��6�   ��5%      �� 5&   "   .R�5'   &   �� $��/image/x.png��6,   5(   #   5*      5+      5,      5.      �� 5/   
   -   5         ��Name$ CandidateName��5/      $
��Left=�6)   $��23��6D   $��	23,50,0,0q�6P    �� TextWrapping$
 Wrap=�6c   $��Candidate name��6w   $	��Top=�6�   $	��361��6�   $
��Bold"�6�   
�������6�   �� 50   
   -   5         ��Name$ close��50      $
 0.85Q�6    $��Button��6/   $
��Left=�6@   $��413,10,-4,0q�6[   $	��Top=�6p   $��21��6�   $��21��6�    J� RenderTransformOrigin$ 0.5,0.5&�6�   ��51      �� 52      .R�53      �� $��/image/xclose.png��6   54      56      �� 57   
   -   5        $ button��57      $��Button��6!   $
��Left=�62   $��399,348,0,0q�6M   $	��Top=�6b   $��41��6z   $��36��6�   ��58      �� 59      .R�5:      �� $��/image/Venstre_DK_V.png��6   5;      5=      �� 5>   
   -   5        $ AddNewCandidate��5>      $��Button��6*   $
��Left=�6;   $��
181,80,0,0q�6V   $	��Top=�6j   $��41��6�   $��36��6�   $ 0.5,0.5&�6�   z�5?      V� 5@      .~�5A      �� �� 5B      �� 5C       �� Angle$ 89.77Q�6&   T� 5D      5E      ��5G      �� 5H      .R�5I      �� $��/image/Arrow_Left.png��6   5J      5L      5M      5N      