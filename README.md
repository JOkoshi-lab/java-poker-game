# Javaポーカーゲーム

Javaでカードゲームの処理を実装したWebアプリケーションです。手札の表示やカードの交換、手札履歴の表示などを含みます。

## 使用技術
Java / Spring Boot / Thymeleaf / HTML / CSS

## 起動方法（Windows）

プロジェクトフォルダをPowerShellで開き、次のコマンドを実行してください。

```powershell
.\mvnw.cmd spring-boot:run
```

アプリのアクセス先：http://localhost:8080/poker

データベースを使用する作品は、起動時にH2のインメモリデータベースを初期化します。再起動すると、登録内容は初期状態に戻ります。