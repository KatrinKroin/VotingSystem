   M S B A M L   `   `   `  ���� G  CVotingSystem, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null+clr-namespace:VotingSystemVotingSystem  %    VotingSystem.AssignedCandidates   W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     @d2http://schemas.microsoft.com/expression/blend/2008     Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     ,localclr-namespace:VotingSystem       � Title$  AssignedCandidates��$��394.296��$	��450�� � WindowStyle$
 None=� � AllowsTransparency .  � WindowStartupLocation$ CenterScreen=�����  �� ImageSource$ /image/bck.png��.��� .+��� $
��Bold"�
�������$��Add new candidate:��$
��Left=�$��
141,24,0,0q�$	��Top=�$	��128���� -    ��Name$ CandudatesList�� J� Opacity$
 0.75Q� �� SelectedIndex$ 0��
�������$��22,119,66,32q�����  �� EndPoint$ 0.5,1&�	 �� 
StartPoint$	 0.5,0&�.��� 
 � Color$
 Black�� � Offset$ 0Q�� $
 	#FF565268��$
 0.46Q����� .R��  � ColumnDefinitions �� $��35 ��� $	��250 ��� $��35 � � RowDefinitions �� �� .+��� $
��Left=����� *CandidatePicture+�� $��Center=�$��0��$��1��
�������$
��Bold"����� *CandidateName+�� $��EditAssignedUsers��-   $��RemoveCandidate��$��31��$
��Left=�$��2��$	��Top=�$��35������ .R��� $��/image/x.png���� -    ��Name$ CandidateName��$
��Left=�$��23��$��	23,50,0,0q� �� TextWrapping$
 Wrap=�$��Candidate name��$	��Top=�$	��361��$
��Bold"�
��������� -    ��Name$ close��$
 0.85Q�$��Button��$
��Left=�$��413,10,-4,0q�$	��Top=�$��21��$��21�� J� RenderTransformOrigin$ 0.5,0.5&����� .R��� $��/image/xclose.png���� -   $ button��$��Button��$
��Left=�$��399,348,0,0q�$	��Top=�$��41��$��36������ .R��� $��/image/Venstre_DK_V.png���� -   $ AddNewCandidate��$��Button��$
��Left=�$��
181,80,0,0q�$	��Top=�$��41��$��36��$ 0.5,0.5&�z�V� .~��� �� ��  �� Angle$ 89.77Q�T� ���� .R��� $��/image/Arrow_Left.png��