import SwiftUI

struct ToastPresenter: ViewModifier {
    @Binding var isPresented: Bool
    let duration: TimeInterval
    let systemImageName: String?
    let message: String
    let tint: Color?
    
    func body(content: Content) -> some View {
        content
            .overlay(alignment: .top) {
                if isPresented {
                    HStack {
                        if let systemImageName {
                            Image(systemName: systemImageName)
                                .foregroundStyle(tint != nil ? .white : Color(uiColor: .label))
                        }
                        Text(message)
                            .font(.subheadline.weight(.semibold))
                            .foregroundStyle(tint != nil ? .white : Color(uiColor: .label))
                    }
                    .padding(.horizontal, 24)
                    .padding(.vertical)
                    .glassEffect(.regular.tint(tint))
                    .transition(.move(edge: .top).combined(with: .opacity))
                    .onAppear {
                        // Auto-dismiss logic moved to onAppear for simplicity in this context
                        // or could use .task as in the article if iOS 15+ is guaranteed (it usually is for SwiftUI)
                        DispatchQueue.main.asyncAfter(deadline: .now() + duration) {
                            withAnimation {
                                isPresented = false
                            }
                        }
                    }
                }
            }
            .animation(.default, value: isPresented)
    }
}

// Glass Effect Implementation
struct Glass {
    var style: UIBlurEffect.Style = .systemUltraThinMaterial
    var tint: Color? = nil
    
    static let regular = Glass(style: .systemMaterial)
    
    func tint(_ color: Color?) -> Glass {
        var copy = self
        copy.tint = color
        return copy
    }
}

extension View {
    func glassEffect(_ glass: Glass) -> some View {
        self.background(
            ZStack {
                VisualEffectView(style: glass.style)
                    .mask(Capsule())
                
                if let tint = glass.tint {
                    tint.opacity(0.1)
                        .clipShape(Capsule())
                }
                
                // Add a border for better visibility against similar backgrounds
                Capsule()
                    .strokeBorder(Color.white.opacity(0.1), lineWidth: 0.5)
            }
            .shadow(color: .black.opacity(0.15), radius: 15, x: 0, y: 8)
        )
    }
    
    func toast(
        isPresented: Binding<Bool>,
        duration: TimeInterval = 3.0,
        systemImageName: String? = nil,
        message: String,
        tint: Color? = nil
    ) -> some View {
        modifier(
            ToastPresenter(
                isPresented: isPresented,
                duration: duration,
                systemImageName: systemImageName,
                message: message,
                tint: tint
            )
        )
    }
}

// Wrapper for UIVisualEffectView in SwiftUI
struct VisualEffectView: UIViewRepresentable {
    var style: UIBlurEffect.Style
    
    func makeUIView(context: Context) -> UIVisualEffectView {
        UIVisualEffectView(effect: UIBlurEffect(style: style))
    }
    
    func updateUIView(_ uiView: UIVisualEffectView, context: Context) {
        uiView.effect = UIBlurEffect(style: style)
    }
}
