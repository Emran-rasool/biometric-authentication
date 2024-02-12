import React from 'react';
import { StyleSheet, Text, View, TouchableOpacity, Alert } from 'react-native';
import { NativeModules } from 'react-native';
 
const biometricAuth = NativeModules.BiometricAuthModule;


function App() {
  
  function handleButtonClick(){

    biometricAuth.isBiometricSupported((isSupported:boolean) => {
      
      if(isSupported) {

        biometricAuth.authenticationPrompt("Biometric Authentication",
        "Authenticate using your biometric credentials","Cancel");
        biometricAuth.authenticate((error:string, message:string) => {
          if (error) {
            Alert.alert(error)
        
          } else {
            Alert.alert(message)
          }
        });
      
      }

  });

  }
  
  return (
    <View style={styles.container}>
    
      <Text style={styles.text}> Welcome to Biometric Authentication </Text>
      <TouchableOpacity style={styles.buttonBackground} onPress={handleButtonClick}>
      <Text style={styles.buttonText}>Biometric Authentication</Text>
    </TouchableOpacity>
    
    </View>

  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    color:"#3498db",
    padding:16,
    marginBottom:16
  },
  buttonBackground: {
    backgroundColor: '#3498db',
    padding: 8,
    borderRadius: 5,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: 'bold',
  },


});

export default App;
