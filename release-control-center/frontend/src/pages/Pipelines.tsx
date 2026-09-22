import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { PipelineRun } from '../types';
import { GitBranch, CheckCircle2, Clock, Container } from 'lucide-react';

export const Pipelines: React.FC = () => {
  const [pipelines, setPipelines] = useState<PipelineRun[]>([]);

  useEffect(() => {
    devopsService.getPipelines().then(setPipelines).catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-2">
          <GitBranch className="w-7 h-7 text-blue-400" />
          <span>GitHub Actions CI Pipelines</span>
        </h1>
        <p className="text-slate-400 text-sm">Automated build, test, Docker image push, and GitOps image tag updates</p>
      </div>

      <div className="bg-slate-900 border border-slate-800 rounded-2xl overflow-hidden">
        <table className="w-full text-left text-sm text-slate-300">
          <thead className="bg-slate-800/80 text-xs font-semibold text-slate-400 uppercase tracking-wider">
            <tr>
              <th className="px-6 py-4">Workflow Name</th>
              <th className="px-6 py-4">Commit SHA</th>
              <th className="px-6 py-4">Branch</th>
              <th className="px-6 py-4">Author</th>
              <th className="px-6 py-4">Status</th>
              <th className="px-6 py-4">Duration</th>
              <th className="px-6 py-4">Docker Image Tag</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800">
            {pipelines.map(pipe => (
              <tr key={pipe.id} className="hover:bg-slate-800/40 transition">
                <td className="px-6 py-4 font-semibold text-slate-200">{pipe.workflowName}</td>
                <td className="px-6 py-4 font-mono text-xs text-blue-400">{pipe.commitSha}</td>
                <td className="px-6 py-4 font-mono text-xs text-slate-400">{pipe.branch}</td>
                <td className="px-6 py-4 text-xs text-slate-300">{pipe.author}</td>
                <td className="px-6 py-4">
                  <span className="px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                    {pipe.conclusion.toUpperCase()}
                  </span>
                </td>
                <td className="px-6 py-4 text-xs text-slate-400">{pipe.durationSeconds}s</td>
                <td className="px-6 py-4 font-mono text-xs text-cyan-400">{pipe.imageTag}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
