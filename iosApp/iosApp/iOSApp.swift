import SwiftUI
import ComposeApp
import Firebase

@main
struct iOSApp: App {

    
    init() {
        FirebaseApp.configure()
        KoinIosKt.doInitKoinIOS()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
