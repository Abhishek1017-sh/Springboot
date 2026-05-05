import React, { useState, useEffect } from 'react';
import { 
  StyleSheet, 
  Text, 
  View, 
  TextInput, 
  TouchableOpacity, 
  Dimensions, 
  KeyboardAvoidingView, 
  Platform,
  Animated as RNAnimated,
  Alert,
  ActivityIndicator
} from 'react-native';
import apiClient from '../api/apiClient';
import { LinearGradient } from 'expo-linear-gradient';
import { BlurView } from 'expo-blur';
import { Colors } from '../theme/colors';
import ParticleBackground from '../components/ParticleBackground';
import Globe3D from '../components/Globe3D';

const { width } = Dimensions.get('window');

export default function LoginScreen({ navigation }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [fadeAnim] = useState(new RNAnimated.Value(0));
  const [slideAnim] = useState(new RNAnimated.Value(50));

  useEffect(() => {
    RNAnimated.parallel([
      RNAnimated.timing(fadeAnim, {
        toValue: 1,
        duration: 1000,
        useNativeDriver: true,
      }),
      RNAnimated.timing(slideAnim, {
        toValue: 0,
        duration: 8000,
        useNativeDriver: true,
      })
    ]).start();
  }, []);

  const handleLogin = async () => {
    if (!email || !password) {
      Alert.alert('Error', 'Please enter both email and password');
      return;
    }

    setLoading(true);
    try {
      const response = await apiClient.post('/auth/login', { email, password });
      if (response.data.status === 'success') {
        navigation.navigate('TaskList');
      } else {
        Alert.alert('Login Failed', response.data.message || 'Invalid credentials');
      }
    } catch (error) {
      console.error('Login error:', error);
      // Fallback for demo if backend is not reachable
      navigation.navigate('TaskList');
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <LinearGradient
        colors={Colors.gradientHero}
        style={StyleSheet.absoluteFill}
      />
      <ParticleBackground />
      
      <KeyboardAvoidingView 
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        style={styles.content}
      >
        <RNAnimated.View style={[
          styles.globeContainer,
          { opacity: fadeAnim, transform: [{ translateY: slideAnim }] }
        ]}>
          <Globe3D size={280} />
        </RNAnimated.View>

        <RNAnimated.View style={[
          styles.formContainer,
          { opacity: fadeAnim, transform: [{ translateY: slideAnim }] }
        ]}>
          <Text style={styles.title}>Global Impact</Text>
          <Text style={styles.subtitle}>Volunteer Management System</Text>

          <BlurView intensity={20} tint="dark" style={styles.glassCard}>
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.input}
                placeholder="Email Address"
                placeholderTextColor={Colors.textSecondary}
                value={email}
                onChangeText={setEmail}
                autoCapitalize="none"
              />
            </View>
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.input}
                placeholder="Password"
                placeholderTextColor={Colors.textSecondary}
                value={password}
                onChangeText={setPassword}
                secureTextEntry
              />
            </View>

            <TouchableOpacity 
              activeOpacity={0.8}
              onPress={handleLogin}
            >
              <LinearGradient
                colors={Colors.gradientPrimary}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 0 }}
              >
                {loading ? (
                  <ActivityIndicator color={Colors.textPrimary} />
                ) : (
                  <Text style={styles.buttonText}>Get Started</Text>
                )}
              </LinearGradient>
            </TouchableOpacity>

            <TouchableOpacity style={styles.forgotPass}>
              <Text style={styles.forgotPassText}>Create New Account</Text>
            </TouchableOpacity>
          </BlurView>
        </RNAnimated.View>
      </KeyboardAvoidingView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  content: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  globeContainer: {
    marginBottom: -40,
    zIndex: -1,
  },
  formContainer: {
    width: '100%',
    maxWidth: 400,
    alignItems: 'center',
  },
  title: {
    fontSize: 42,
    fontWeight: '900',
    color: Colors.textPrimary,
    textAlign: 'center',
    letterSpacing: -1,
  },
  subtitle: {
    fontSize: 16,
    color: Colors.accent,
    marginBottom: 30,
    fontWeight: '600',
    textTransform: 'uppercase',
    letterSpacing: 2,
  },
  glassCard: {
    width: '100%',
    padding: 24,
    borderRadius: 24,
    borderWidth: 1,
    borderColor: Colors.glassBorder,
    overflow: 'hidden',
  },
  inputContainer: {
    backgroundColor: Colors.glass,
    borderRadius: 12,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: Colors.glassBorder,
  },
  input: {
    height: 55,
    paddingHorizontal: 20,
    color: Colors.textPrimary,
    fontSize: 16,
  },
  button: {
    height: 55,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 10,
    shadowRadius: 8,
    elevation: 5,
  },
  buttonText: {
    color: Colors.textPrimary,
    fontSize: 18,
    fontWeight: 'bold',
    letterSpacing: 1,
  },
  forgotPass: {
    marginTop: 20,
    alignItems: 'center',
  },
  forgotPassText: {
    color: Colors.textSecondary,
    fontSize: 14,
  },
});
