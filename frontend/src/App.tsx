function App() {
  return (
    <main className="min-h-screen bg-slate-950 px-6 py-20 text-white">
      <section className="mx-auto max-w-3xl">
        <p className="text-sm font-semibold uppercase tracking-[0.25em] text-cyan-400">
          Offline-first collaboration
        </p>

        <h1 className="mt-4 text-5xl font-bold tracking-tight">
          MergeMesh
        </h1>

        <p className="mt-4 max-w-xl text-lg leading-8 text-slate-300">
          Inventory collaboration that keeps working when your connection
          doesn't.
        </p>

        <div className="mt-10 rounded-xl border border-slate-700 bg-slate-900 p-5">
          <p className="font-semibold text-emerald-400">
            Frontend initialized
          </p>
          <p className="mt-2 text-sm text-slate-300">
            React, TypeScript, Vite, and Tailwind CSS are connected.
          </p>
        </div>
      </section>
    </main>
  )
}

export default App