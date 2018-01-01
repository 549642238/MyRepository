// Huffman coding
#include <iostream>
#include <string>
#include <limits>
#include <deque>
using namespace std;

struct Node{
	Node *left, *right;
	unsigned int weight;
	unsigned char sig;
	char val;
	Node():val(0), sig(0), weight(0), left(NULL), right(NULL){}
};

void DFS(Node* root, string path){
	if(root->left == NULL && root->right == NULL){
		cout<<root->val<<" -> "<<path<<endl;
	}else{
		if(root->left){
			DFS(root->left, path+"0");
		}
		if(root->right){
			DFS(root->right, path+"1");
		}
	}
}

int main(){
	deque<Node> information;
	unsigned int N;
	cin>>N;
	for(int i=0;i<N;i++){
		Node node;
		cin>>node.val>>node.weight;
		node.sig = 0;
		information.push_back(node);
	}
	for(int i=0;i<N-1;i++){
		unsigned int min1 = numeric_limits<unsigned int>::max();
		unsigned int min2 = numeric_limits<unsigned int>::max();
		unsigned int minPos1=0, minPos2=0;
		for(int j=0;j<information.size();++j){
			if(!(information[j].sig) && information[j].weight < min1){
				min2 = min1;
				minPos2 = minPos1;
				min1 = information[j].weight;
				minPos1 = j;
			}else if(!(information[j].sig) && information[j].weight < min2){
				min2 = information[j].weight;
				minPos2 = j;
			}
		}
		information[minPos1].sig = 1;
		information[minPos2].sig = 1;
		Node parent;
		parent.weight = information[minPos1].weight + information[minPos2].weight;
		parent.left = (Node*)&(information[minPos1]);
		parent.right = (Node*)&(information[minPos2]);
		parent.val = -1;
		parent.sig = 0;
		information.push_back(parent);
	}
	Node root = information.back();
	DFS(&root, "");
	return 0;
}
