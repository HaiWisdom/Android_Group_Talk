
service MessageService{
	void registerX(1: string userId, 2: string password);
	string loginX(1: string userId);
	void joinX(1: string userId, 2: string groupId);
	void quitX(1: string userId, 2: string groupId);
	list<string> transmitMessage(1: string userId, 2: string groupId, 3: string message);
	list<string> checkPulse(1: string userId, 2: string groupId);
	set<string> checkUser(1: string groupId);
}
