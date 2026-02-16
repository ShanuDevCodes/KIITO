import SwiftUI
import Combine

class ToastManager: ObservableObject {
    @Published var isPresented: Bool = false
    @Published var message: String = ""
    @Published var systemImageName: String? = nil
    @Published var tint: Color? = nil
    
    func show(message: String, systemImageName: String? = nil, tint: Color? = nil) {
        DispatchQueue.main.async {
            self.message = message
            self.systemImageName = systemImageName
            self.tint = tint
            self.isPresented = true
        }
    }
}
