import React from 'react';
import { StyleSheet, Text, View, TouchableOpacity, Alert } from 'react-native';
import { NativeModules } from 'react-native';

const biometricAuth = NativeModules.BiometricAuthModule;


function App() {

  function handleButtonClick() {

    biometricAuth.isBiometricSupported((errorCode: string) => {

      switch (errorCode) {
        case 'BIOMETRIC_SUCCESS':
          biometricAuthenticationPrompt();
          break;
        case 'BIOMETRIC_STATUS_UNKNOWN':
          Alert.alert('BIOMETRIC_STATUS_UNKNOWN')
          break;

        case 'BIOMETRIC_ERROR_UNSUPPORTED':
          Alert.alert('BIOMETRIC_ERROR_UNSUPPORTED')
          break;

        case 'BIOMETRIC_ERROR_HW_UNAVAILABLE':
          Alert.alert('BIOMETRIC_ERROR_HW_UNAVAILABLE')
          break;

        case 'BIOMETRIC_ERROR_NONE_ENROLLED':
          Alert.alert('BIOMETRIC_ERROR_NONE_ENROLLED')
          break;

        case 'BIOMETRIC_ERROR_NO_HARDWARE':
          Alert.alert('BIOMETRIC_ERROR_NONE_ENROLLED')
          break;

        default:
          Alert.alert('Unexpected error code:', errorCode)
      }


    });

  }


  function biometricAuthenticationPrompt() {

    biometricAuth.authenticationPrompt("Biometric Authentication",
      "Authenticate using your biometric credentials", "Cancel");
    biometricAuth.authenticate((error: string, success: string,failed:string) => {
      if (error) {
        Alert.alert(error)

      } else if(success) {
        Alert.alert(success)
      }else {
        Alert.alert(failed)
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
    color: "#3498db",
    padding: 16,
    marginBottom: 16
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
